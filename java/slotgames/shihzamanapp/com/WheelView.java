package slotgames.shihzamanapp.com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.View;

/**
 * Отвечает за рисование колеса игрового автомата.
 * @author maniselvaraj
 *
 */
public class WheelView extends View implements SlotReelScroller.ScrollingListener {
	
	Paint mWhiteBackgroundPaint;
	
	/** Цвета верхних и нижних теней */
	private int[] SHADOWS_COLORS = new int[] { 0xFF111111,
			0x00AAAAAA, 0x00AAAAAA };

	/** Чтобы создать эффект рамки вокруг колеса обзора, задайте значение отступа для левого и правого полей. */
	private static final int FRAME_PADDING = 10;
	
	/** Высота слота **/
	int itemHeight;
	
	/** Смещение X, чтобы начать рисовать элемент слота. Используется, чтобы не рисовать на рамке. */
	int itemX;

	int mViewFullWidth;
	int mViewFullHeight;
	int mViewWidth;
		
	//Список слотов, которые хранятся в этом колесе.
	List<DrawSlotItem> mSlotItems;
		
	// Рисунки теней
	private GradientDrawable topShadow;
	private GradientDrawable bottomShadow;

	//Выдвижная рамка фона
	private Drawable wheelFrame;
	
	private Rect mFullViewRect;
	
	//Скроллер
	private SlotReelScroller mReelScroller;
	
	//По умолчанию для видимых элементов слота установлено значение 1.
	private int visibleSlotItems = 1;
	
	//Определяет направление прокрутки
	private boolean scrollDown = false;
	
	//Позволяет проверить, ставить ли элементы слота посередине после прокрутки.
	private boolean checkForMiddling = true;
	
	/**
	 * Слушатель, который сообщает родителю позицию элемента слота, который
	 * находится посередине в центре после свитка.
	 */
	interface OnScrollFinishedListener {
		void onWheelFinishedScrolling(int position);
	}
	
	private OnScrollFinishedListener mScrollFinishedListener;
	
	/**
	 * Конструктор
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * Конструктор
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * Конструктор
	 */
	public WheelView(Context context) {
		super(context);
		init(context);
	}
	
	void init(Context context) {
		
		mFullViewRect = new Rect();
		
		mReelScroller = new SlotReelScroller(context, this);
		
		mWhiteBackgroundPaint = new Paint();
		mWhiteBackgroundPaint.setColor(Color.WHITE);

		topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
		bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
		
		mSlotItems = new ArrayList<DrawSlotItem>();		
	}
	
	public void setSlotItems(List<ISlotMachineItem> items) {
		fillSlotDrawItems(items);
	}
	
	void fillSlotDrawItems(List<ISlotMachineItem> slotItems)  {
		mSlotItems.clear();
		int position = 1;
		for (ISlotMachineItem item: slotItems) {			
			mSlotItems.add(new DrawSlotItem(item, position));	
			position++;
		}
	}

	public void setNumberOfVisibleItems(int visible) {
		visibleSlotItems = visible;
	}
	
	public void scroll(int distance, int duration) {		
		if (distance != 0 ) {
			checkForMiddling = true;
			mReelScroller.scroll(distance, duration);
		}
	}
	
	public void setScrollFinishedListener(OnScrollFinishedListener listener) {
		mScrollFinishedListener = listener;
	}
	
	/**
	 * Устанавливает возможность рисования для фона колеса
	 * @param resource
	 */
	public void setWheelBackground(Drawable resource) {
		wheelFrame = resource;
	}

	public void setWheelScrollingDirection(boolean scrollDown) {
		this.scrollDown = scrollDown;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//Полная ширина просмотра
		mViewFullWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		mViewFullHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		
		//Ширина без рамок
		mViewWidth = mViewFullWidth - (2 * FRAME_PADDING);
		int viewHeight = mViewFullHeight- (2 * FRAME_PADDING);
		itemHeight = viewHeight / visibleSlotItems;
		
		setCorrectVisibleItems();
		resetSlotItemsPositions(scrollDown);

		itemX = FRAME_PADDING;
		
    	mFullViewRect.top = FRAME_PADDING;
    	mFullViewRect.left = FRAME_PADDING;
    	mFullViewRect.right = mViewWidth + FRAME_PADDING;
    	mFullViewRect.bottom = mViewFullHeight - FRAME_PADDING;    	

    	setMeasuredDimension(mViewFullWidth, mViewFullHeight);
	}

    /**
     *  Устанавливает правильное количество видимых mSlotItems.
     */
    void setCorrectVisibleItems() {
    	if (visibleSlotItems == 0  || visibleSlotItems == mSlotItems.size()) {
    		visibleSlotItems = mSlotItems.size()-1;
    	}
    }
    
	public void onDraw(Canvas canvas) {

		//Нарисуйте фон кадра
		drawFrame(canvas);

		//Нарисуйте белый фон
		canvas.drawRect(mFullViewRect, mWhiteBackgroundPaint);
		
		//Нарисуйте все элементы слота.
		drawSlotItems(canvas);	
		
		//Нарисуйте верхнюю и нижнюю тени
		drawShadows(canvas);
	}
	
	private void drawSlotItems(Canvas canvas) {
		
		for (DrawSlotItem item: mSlotItems) {
			View view = item.getView();
			
			//Измерьте вид по точным размерам.
	        int widthSpec = MeasureSpec.makeMeasureSpec(mViewWidth, MeasureSpec.EXACTLY);
	        int heightSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
	        view.measure(widthSpec, heightSpec);

	        //Разложите вид по ширине и высоте прямоугольника
	        view.layout(itemX, item.getY() + FRAME_PADDING, mViewWidth, item.getY()+itemHeight);

	        //Переместите холст на место и нарисуйте его.
	        canvas.save();
	        canvas.translate(itemX, item.getY()+FRAME_PADDING);
	        view.draw(canvas);
	        canvas.restore();
		}		
	}
	
	/**
	 * Нарисуйте рамку вокруг элементов слота.
	 * @param canvas
	 */
	private void drawFrame(Canvas canvas) {
		if (wheelFrame != null) {
			wheelFrame.setBounds(0, 0, mViewFullWidth, mViewFullHeight);
			wheelFrame.draw(canvas);
		}
	}
	
	/**
	 * Draws shadows on top and bottom of control
	 * @param canvas the canvas for drawing
	 */
	private void drawShadows(Canvas canvas) {
		int height = (int)(1.0 * itemHeight);
		topShadow.setBounds(0, 0, getWidth(), height);
		topShadow.draw(canvas);

		bottomShadow.setBounds(0, getHeight() - height, getWidth(), getHeight());
		bottomShadow.draw(canvas);
	}

	/**
	 * Класс-помощник для удержания позиции Y каждого элемента слота.
	 * Прокрутка достигается увеличением / уменьшением
	 * позиция Y каждого элемента слота и нарисована на каждом свитке (дельта)
	 * получается из скроллера.
	 *
	 */
	class DrawSlotItem {
		int positionY;
		View view;
		int slotPosition;
		
		public DrawSlotItem(ISlotMachineItem item, int slotPosition) {
			view = item.getView();
			this.slotPosition = slotPosition;
		}
		
		public void setPosY(int y) {
			this.positionY = y;
		}
		
		public int getY() {
			return positionY;
		}

		public View getView() {
			return view;
		}
		
		public int getSlotPosition() {
			return slotPosition;
		}
	}
	
	public void scroll(int delta) {
		
		boolean scrollDown = delta < 0;
		//обновить позиции y всех mSlotItems.
		for(DrawSlotItem item: mSlotItems) {
			item.setPosY(item.getY()-delta);
		}

		//Проверьте, не выходит ли позиция item1 за пределы экрана.
		//Поменять местами все mSlotItems.Set Y = itemheight;
		int hiddingItemY;
		if (scrollDown) {
			hiddingItemY = mSlotItems.get(visibleSlotItems-1).getY();
			if (hiddingItemY >= mViewFullHeight) {
				int j = 0;
				for(int i = 1; i < mSlotItems.size(); i++) {
					Collections.swap(mSlotItems, j, i);
					j++;
				}
				resetSlotItemsPositions(scrollDown);
			}
		} else {
			hiddingItemY = mSlotItems.get(0).getY();
			if (hiddingItemY <= -itemHeight) {
				int j = 0;
				for(int i = 1; i < mSlotItems.size(); i++) {
					Collections.swap(mSlotItems, j, i);
					j++;
				}
				resetSlotItemsPositions(scrollDown);
			}

		}
		
		invalidate();
	}
	
	void resetSlotItemsPositions(boolean scrollDown) {
		int multipler = scrollDown ? -1 : 0;
		for(DrawSlotItem item: mSlotItems) {			
			item.setPosY(FRAME_PADDING + itemHeight * multipler);
			multipler++;
		}
	}
	
	@Override
	public void onScroll(int distance) {
		scroll(distance);
	}

	@Override
	public void onFinished() {
		if (checkForMiddling == true) {
			positionNearestMiddleItem();
			checkForMiddling = false;
		}		
	}
	
	/**
	 * Посередине последний видимый элемент после прокрутки.
	 */
	private void positionNearestMiddleItem() {		
		// Высота середины обзора
		int viewCenter = (mViewFullHeight - ( 2 * FRAME_PADDING))/2;
		int distance = mSlotItems.get(visibleSlotItems).getY() - ( viewCenter - (itemHeight/2));
		scroll(distance, 1000);
		if (mScrollFinishedListener != null) {
			mScrollFinishedListener.onWheelFinishedScrolling(mSlotItems.get(visibleSlotItems).getSlotPosition());
		}
 	}
}
