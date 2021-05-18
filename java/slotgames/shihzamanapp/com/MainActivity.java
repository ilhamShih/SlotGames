package slotgames.shihzamanapp.com;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String ROBOTO_BOLD = "Roboto-Bold.ttf";
    private final String VAREL_REGULAR = "VarelaRound-Regular.ttf";

    private final int SPIN_TIME = 7500;
    private final int MESSAGE_CHECK_MATCH = 0;
	Random random;

	int randomMultipler ;
	int SPIN_TIME1 ;
	int SPIN ;
	int S ;
	int S2;
	int S3;


    TextView mResultsText;
    
	WheelView wheelView1;
	WheelView wheelView2;
	WheelView wheelView3;
	
	List<ISlotMachineItem> slotItems1;
	List<ISlotMachineItem> slotItems2;
	List<ISlotMachineItem> slotItems3;


	
	       				//=========== Изображения предметов слота===================//


	private final int slotItem1Images[] = new int[] {
			R.drawable.seven,		// 1
			R.drawable.banane,	    // 2
			R.drawable.big_win,		// 3
			R.drawable.lemon,		// 4
			R.drawable.orange,		// 5
			R.drawable.cherry,		// 6
			R.drawable.plum			// 7


	};

	private final int slotItem2Images[] = new int[] {
			R.drawable.lemon,		// 1
			R.drawable.seven,   	// 2
			R.drawable.plum,		// 3
			R.drawable.cherry,		// 4
			R.drawable.banane,		// 5
			R.drawable.big_win,		// 6
			R.drawable.orange		// 7
	};

	private final int slotItem3Images[] = new int[] {
			R.drawable.plum,		// 1
			R.drawable.orange,		// 2
			R.drawable.big_win,		// 3
			R.drawable.seven,		// 4
			R.drawable.lemon,		// 5
			R.drawable.banane,		// 6
			R.drawable.cherry		// 7
	};

	//======================Тексты слотов========================//

	private final int slotItem1Texts[] = new int[] {
			R.string.seven,			// 1
			R.string.banane,		// 2
			R.string.big_win,		// 3
			R.string.lemon,			// 4
			R.string.orange,		// 5
			R.string.cherry,		// 6
			R.string.plum			// 7


	};

	private final int slotItem2Texts[] = new int[] {
			R.string.lemon,			// 1
			R.string.seven,   		// 2
			R.string.plum,			// 3
			R.string.cherry,		// 4
			R.string.banane,		// 5
			R.string.big_win,		// 6
			R.string.orange			// 7

	};

	private final int slotItem3Texts[] = new int[] {
			R.string.plum,			// 1
			R.string.orange,		// 2
			R.string.big_win,		// 3
			R.string.seven,			// 4
			R.string.lemon,			// 5
			R.string.banane,		// 6
			R.string.cherry			// 7

	};

	private int wheel1Selection;
	private int wheel2Selection;
	private int wheel3Selection;
	
	Typeface varelRegularFont;
	Typeface robotoBoldFont;
	
	Button spin;
	
	MediaPlayer mediaPlayer,mediaPlayer2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		
		if(getSupportActionBar() != null) {
			getSupportActionBar().setTitle(getActionBarSpannableTitle(
					this.getResources().getString(R.string.app_name)));
		}





		
		random = new Random();
		mediaPlayer = MediaPlayer.create(this, R.raw.casinobutton21);
		mediaPlayer2 = MediaPlayer.create(this, R.raw.casinomone);

		randomMultipler = random.nextInt(55);
		SPIN_TIME1 = ThreadLocalRandom.current().nextInt(3000,5000);
		SPIN = ThreadLocalRandom.current().nextInt(100,200);
		S = ThreadLocalRandom.current().nextInt(10,15);
		S2 = ThreadLocalRandom.current().nextInt(20,25);
		S3 = ThreadLocalRandom.current().nextInt(30,35);

		//Toast.makeText(getApplicationContext(), 70000 + ( SPIN - S2 * randomMultipler), Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), 45000 + ( SPIN - S3 * randomMultipler), Toast.LENGTH_LONG).show();

		robotoBoldFont = Typeface.createFromAsset(getAssets(),ROBOTO_BOLD );
		varelRegularFont = Typeface.createFromAsset(getAssets(),VAREL_REGULAR );
		
		TextView mWelcomeText = (TextView) findViewById(R.id.welcomeTxt);		
		mWelcomeText.setTypeface(robotoBoldFont);
		
		mResultsText = (TextView) findViewById(R.id.resultTxt);		
		mResultsText.setTypeface(robotoBoldFont);
		mResultsText.setVisibility(View.INVISIBLE);
		
		wheelView1 = (WheelView) findViewById(R.id.wheel1);
		wheelView2 = (WheelView) findViewById(R.id.wheel2);
		wheelView3 = (WheelView) findViewById(R.id.wheel3);
		
		//Создайте слоты для каждого колеса.
		slotItems1 = new ArrayList<ISlotMachineItem>();		
		slotItems2 = new ArrayList<ISlotMachineItem>();
		slotItems3 = new ArrayList<ISlotMachineItem>();
		
				//==================Установите элементы слота для каждого колеса.==================//
		slotItems1.add(new SlotItemsImpl(1,0));
		slotItems1.add(new SlotItemsImpl(1,1));
		slotItems1.add(new SlotItemsImpl(1,2));
		slotItems1.add(new SlotItemsImpl(1,3));
		slotItems1.add(new SlotItemsImpl(1,4));
		slotItems1.add(new SlotItemsImpl(1,5));
		slotItems1.add(new SlotItemsImpl(1,6));
		wheelView1.setSlotItems(slotItems1);
		
		slotItems2.add(new SlotItemsImpl(2,0));
		slotItems2.add(new SlotItemsImpl(2,1));
		slotItems2.add(new SlotItemsImpl(2,2));
		slotItems2.add(new SlotItemsImpl(2,3));
		slotItems2.add(new SlotItemsImpl(2,4));
		slotItems2.add(new SlotItemsImpl(2,5));
		slotItems2.add(new SlotItemsImpl(2,6));
		wheelView2.setSlotItems(slotItems2);

		slotItems3.add(new SlotItemsImpl(3,0));
		slotItems3.add(new SlotItemsImpl(3,1));
		slotItems3.add(new SlotItemsImpl(3,2));
		slotItems3.add(new SlotItemsImpl(3,3));
		slotItems3.add(new SlotItemsImpl(3,4));
		slotItems3.add(new SlotItemsImpl(3,5));
		slotItems3.add(new SlotItemsImpl(3,6));
		wheelView3.setSlotItems(slotItems3);

		wheelView1.setNumberOfVisibleItems(2);
		wheelView2.setNumberOfVisibleItems(2);
		wheelView3.setNumberOfVisibleItems(2);
		
		wheelView1.setWheelBackground(getResources().getDrawable(R.drawable.wheel_frame));
		wheelView2.setWheelBackground(getResources().getDrawable(R.drawable.wheel_frame));
		wheelView3.setWheelBackground(getResources().getDrawable(R.drawable.wheel_frame));

		wheelView1.setScrollFinishedListener(new WheelView.OnScrollFinishedListener() {			
			@Override
			public void onWheelFinishedScrolling(int position) {
				wheel1Selection = position;
			}
		});

		wheelView2.setScrollFinishedListener(new WheelView.OnScrollFinishedListener() {			
			@Override
			public void onWheelFinishedScrolling(int position) {
				wheel2Selection = position;
			}
		});

		wheelView3.setScrollFinishedListener(new WheelView.OnScrollFinishedListener() {			
			@Override
			public void onWheelFinishedScrolling(int position) {
				wheel3Selection = position;
			}
		});


		//################################################################################################################
				////////=========================== Клик По Кнопке Играть ==================================//////
		spin = (Button) findViewById(R.id.spin);
		spin.setTypeface(varelRegularFont);
		
		spin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mediaPlayer.start();
				spin.setEnabled(false);
				spin.setBackgroundResource(R.color.sold);
				mResultsText.setVisibility(View.INVISIBLE);

				// Измените диапазон времени и расстояния, чтобы получить
				//случайность прокрутки колеса.
				wheelView1.scroll(6000 + ( SPIN - S * randomMultipler), SPIN_TIME1);
				//wheelView1.scroll(25000, SPIN_TIME1);
                //int t =( 45000 + ( SPIN - S * randomMultipler));
				//Log.d("INFO1", String.valueOf(t));


				wheelView2.scroll(10000 + ( SPIN - S2 * randomMultipler), SPIN_TIME1);
				//wheelView2.scroll(32500 , SPIN_TIME1);
				//int t1 =( 70000 + ( SPIN - S2 * randomMultipler));
				//Log.d("INFO1", String.valueOf(t1));

				wheelView3.scroll(8000 + ( SPIN - S3 * randomMultipler), SPIN_TIME1);
				//wheelView3.scroll(25000, SPIN_TIME1);
				//int t2 =( 45000 + ( SPIN - S3 * randomMultipler));
				//Log.d("INFO1", String.valueOf(t2));


				Message msg = Message.obtain();
				msg.what = MESSAGE_CHECK_MATCH;
				detectAnyMatchHandler.sendMessageDelayed(msg, SPIN_TIME1 + 800);
			}
		});

		         ////////=========================== Клик По Кнопке Играть ==================================//////
		//################################################################################################################


		// Установите ширину колес на основе общей ширины экрана.
		// Так что он хорошо рассчитывается на всех устройствах.
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
				
		final float density = getResources().getDisplayMetrics().density;
		int marginBetweenWheels = (int) (5 * density + 0.5f);
		int wheelWidth;
		int wheelHeight;
		boolean isPortrait = false;;
		
		if (width > height) {
			//Пейзаж: занимает 70% ширины экрана
			width = (width * 65 )/100;
			wheelHeight = (height * 60)/100;
		} else {
			isPortrait = true;
			//Портрет: возьмите 90% ширины экрана
			width = (width * 90 )/100;
			wheelHeight = (height * 45)/100;
		}
		
		//Вычтите разницу между двумя колесами
		width -= ( 2 * marginBetweenWheels);
		
		//Разделите общее количество колес (в данном игровом автомате 3).
		wheelWidth = width / 3;			
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wheelView1.getLayoutParams();
		params.width = wheelWidth;
		params.height = wheelHeight;
		
		params = (LinearLayout.LayoutParams) wheelView2.getLayoutParams();
		params.width = wheelWidth;
		params.height = wheelHeight;
		params.leftMargin = marginBetweenWheels;
		
		params = (LinearLayout.LayoutParams) wheelView3.getLayoutParams();
		params.width = wheelWidth;
		params.height = wheelHeight;
		params.leftMargin = marginBetweenWheels;
		
		View thickLine1 = (View) findViewById(R.id.thickLine1);
		View thickLine2 = (View) findViewById(R.id.thickLine2);
		RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) thickLine1.getLayoutParams();
		params1.width = width + ( 2 * marginBetweenWheels);
		
		params1 = (RelativeLayout.LayoutParams) thickLine2.getLayoutParams();
		params1.width = width + ( 2 * marginBetweenWheels);		
		
		if (isPortrait == false) {
			//Если альбомная, установите ширину для макета результата
			params1 = (RelativeLayout.LayoutParams) ((View) findViewById(R.id.slotSpinLayout)).getLayoutParams();
			params1.width = wheelWidth + wheelWidth/2;
		}
	}
	
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.release();
		mediaPlayer2.release();
	}
		
	@SuppressLint("HandlerLeak")
	private Handler detectAnyMatchHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_CHECK_MATCH) {
				spin.setEnabled(true);
				spin.setBackgroundResource(R.color.available);

				if (wheel1Selection == 1 && wheel2Selection == 2 && wheel3Selection == 4) {

					//seven
                    mediaPlayer2.start();
					setSuccessResultMessage(1);

				} else if (wheel1Selection == 2 && wheel2Selection == 5 && wheel3Selection == 6) {

					//banane,
                    mediaPlayer2.start();
					setSuccessResultMessage(2);

				} else if (wheel1Selection == 3 && wheel2Selection == 6 && wheel3Selection == 3) {

					//big_win,
                    mediaPlayer2.start();
					setSuccessResultMessage(3);

				} else if (wheel1Selection == 4 && wheel2Selection == 1 && wheel3Selection == 5) {

					//lemon
                    mediaPlayer2.start();
					setSuccessResultMessage(4);

				} else if (wheel1Selection == 5 && wheel2Selection == 7 && wheel3Selection == 2) {

					//orange
                    mediaPlayer2.start();
					setSuccessResultMessage(5);

				} else if (wheel1Selection == 6 && wheel2Selection == 4 && wheel3Selection == 7) {

					//cherry
                    mediaPlayer2.start();
					setSuccessResultMessage(6);

				} else if (wheel1Selection == 7 && wheel2Selection == 3 && wheel3Selection == 1) {

					//plum
                    mediaPlayer2.start();
					setSuccessResultMessage(7);

				} else {

					mResultsText.setVisibility(View.VISIBLE);
					mResultsText.setText(MainActivity.this.getResources().getString(R.string.try_again_text));
				}
			}
		}
	};
	//################################################################################################################
	////////=========================== Реализация интерфейса ISlotMachineItem ==================================//////
	class SlotItemsImpl implements ISlotMachineItem {

		int wheelPos;
		int slotItemPos;
		
		public SlotItemsImpl(
				int wheelPos,
				int slotItemPos) {
				this.wheelPos = wheelPos;
				this.slotItemPos = slotItemPos;
		}
		
		@Override
		public View getView() {
			View view = (View) getLayoutInflater().inflate(R.layout.slot_item_layout, null, false);
			ImageView itemImageView = (ImageView) view.findViewById(R.id.itemImage);
			TextView  itemTextView  = (TextView)  view.findViewById(R.id.itemTxt);
			itemTextView.setTypeface(varelRegularFont);
			
			Resources resources = getResources();
			if (wheelPos == 1) {
				itemImageView.setImageResource(slotItem1Images[slotItemPos]);
				itemTextView.setText(resources.getString(slotItem1Texts[slotItemPos]));
			} else if (wheelPos == 2) {
				itemImageView.setImageResource(slotItem2Images[slotItemPos]);
				itemTextView.setText(resources.getString(slotItem2Texts[slotItemPos]));
			} else if (wheelPos == 3) {
				itemImageView.setImageResource(slotItem3Images[slotItemPos]);
				itemTextView.setText(resources.getString(slotItem3Texts[slotItemPos]));
			}
			return view;
		}
	}
	////////=========================== Реализация интерфейса ISlotMachineItem ==================================//////
	//################################################################################################################

	
    public SpannableString getActionBarSpannableTitle(String title) {
        SpannableString span = new SpannableString(title);
        span.setSpan(new slotgames.shihzamanapp.com.TypefaceSpan(this, ROBOTO_BOLD ), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
    
    private void setSuccessResultMessage(int position) {
    	
    	mResultsText.setVisibility(View.VISIBLE);
    	String resultString = null;    	
        int startPos = 0;
        int endPos = 0;
        String beverage = null;
        Resources res = getResources();
    	
        if (position == 1) {

    		beverage = res.getString(R.string.seven);


    	} else if (position == 2) {
			beverage = res.getString(R.string.banane);


    	} else if (position == 3) {
			beverage = res.getString(R.string.big_win);


		} else if (position == 4) {

			beverage = res.getString(R.string.lemon);


		} else if (position == 5) {

			beverage = res.getString(R.string.orange);

		} else if (position == 6) {

			beverage = res.getString(R.string.cherry);

		} else if (position == 7) {

			beverage = res.getString(R.string.plum);

    	} else {

    		mResultsText.setVisibility(View.INVISIBLE);
    		return;
    	}
    	
		resultString =  beverage;
		startPos = resultString.indexOf(beverage, 0);
		endPos = startPos + beverage.length();

    	Spannable wordToSpan = new SpannableString(resultString);
        wordToSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.button_start_color)), startPos,
        		endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new RelativeSizeSpan(1.5f), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);        
        mResultsText.setText(wordToSpan);

    }
}
