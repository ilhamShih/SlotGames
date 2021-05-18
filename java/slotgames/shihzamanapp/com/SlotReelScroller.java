package slotgames.shihzamanapp.com;

import android.content.Context;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * Отвечает за создание значений прокрутки в зависимости от заданного расстояния и времени.
 * Использует класс Scroller для создания значений прокрутки.
 * 
 * @author maniselvaraj
 *
 */
public class SlotReelScroller implements Runnable {

    /**
     * Прокрутка интерфейса слушателя
     */
    public interface ScrollingListener {
        /**
         * Обратный вызов прокрутки вызывается при выполнении прокрутки.
         * @ param расстояние для прокрутки
         */
        void onScroll(int distance);

        /**
         * Завершение обратного звонка.
         */
        void onFinished();        
    }

    private Handler mHandler;
    private Scroller mScroller;
    private ScrollingListener mScrollListener;
    int lastY = 0;
    private int distance;
    private int previousDistance;
    
    public SlotReelScroller(Context context, ScrollingListener listener) {
    	mHandler = new Handler();
    	mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
    	//mScroller = new Scroller(context, new AccelerateInterpolator());
    	//mScroller = new Scroller(context);
    	mScrollListener = listener;
    }
    
    public void scroll(int distance, int duration) {
    	this.distance = distance;
        mScroller.forceFinished(true);
    	mScroller.startScroll(0, 0, 0, distance, duration);
    	mHandler.post(this);

    }

    public void run() {		
		int delta = 0;
		mScroller.computeScrollOffset();
		int currY = mScroller.getCurrY();

		delta = currY - lastY;
		lastY = currY;	
		
		if (Math.abs(delta) != previousDistance && delta != 0) {					  
			mScrollListener.onScroll(delta);
		}			
		
		if (mScroller.isFinished() == false) {
			//Опубликуйте этот исполняемый файл снова в потоке пользовательского интерфейса,
            // пока не будут прочитаны все значения прокрутки.
			mHandler.post(this);
		} else {
			previousDistance = distance;
			mScrollListener.onFinished();
		}
    }
}
