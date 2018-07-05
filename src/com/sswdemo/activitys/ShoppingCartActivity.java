package com.sswdemo.activitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.sswdemo.loadimage.LoadImage;
import com.sswdemo.model.Product;
import com.sswdemo.model.Shop;
import com.sswdemo.model.ShoppingCart;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.servcie.ShoppingCartDBService;

public class ShoppingCartActivity extends Activity implements OnClickListener{

	protected static final String TAG = "ssw";

	protected static final int FLASH_PRICE = 1;

	private ListView listView;
	private ShoppingCartAdapter adapter;
	private List<ShoppingCart> shoppingCarts = new ArrayList<ShoppingCart>();
	private LayoutInflater  inflater;
	private TextView totalPriceTV;
	private CheckBox totalPriceCB;
	private LinearLayout floatShopLinear;

	/**
	 * state of title.
	 * @author mfy
	 */
	public enum TitleState{
		TITLE_INVISIBLE,	// do not show 
		TITLE_VISIBLE,		// show
		TITLE_FLOAT    		// float
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case FLASH_PRICE:
				adapter.notifyDataSetChanged();
				flashPrice();
				break;
			}
		};
	};

	private Button checkBtn;

	private ImageButton backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_shopping_cart);

		initView();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		flashPrice();
	}

	private void initView(){
		totalPriceTV = (TextView)findViewById(R.id.total_price_tv);
		totalPriceCB = (CheckBox)findViewById(R.id.total_price_cb);
		listView = (ListView)findViewById(R.id.shopping_cart_listview);
		floatShopLinear = (LinearLayout)findViewById(R.id.shop_title_layout);
		checkBtn = (Button)findViewById(R.id.check_btn);
		backBtn = (ImageButton)findViewById(R.id.back_btn);
		adapter = new ShoppingCartAdapter(this, 0, R.layout.view_shopping_cart_item, shoppingCarts);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
		listView.setCacheColorHint(0);

		listView.setOnScrollListener(adapter);
		totalPriceCB.setOnCheckedChangeListener(totalPriceOnCheckedListener);
		checkBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	private void initData() {
		ShoppingCartDBService scdb = new ShoppingCartDBService(this);
		try {
			adapter.update(scdb.getAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get total price
	 */
	protected void flashPrice() {
		float price = 0;
		for(ShoppingCart sc : shoppingCarts){
			if(!sc.isTitle() && sc.isChecked()){
				price += sc.getProduct().getPrice() * sc.getProduct().getCount();
			}
		}
		totalPriceTV.setText(price+"");
	}

	OnCheckedChangeListener totalPriceOnCheckedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			for (ShoppingCart sc : shoppingCarts) {
				sc.setChecked(isChecked);
			}
			handler.sendEmptyMessage(FLASH_PRICE);
		}
	};


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.check_btn:

			LoginManager lm = new LoginManager(this);
			if(lm.getUserId()==-1){
				//jump to LoginActivity
				Intent i = new Intent(ShoppingCartActivity.this, LoginActivity.class);
				startActivityForResult(i, 0);
				return;	
			}
			jumpToSubmitActivity();

			break;
		case R.id.back_btn:
			finish();
			break;
		}

	}

	private void jumpToSubmitActivity() {
		Intent intent = new Intent(this, SubmitOrdersActivity.class);
		List<Shop> shopList = new ArrayList<Shop>();
		Shop shop = null;
		List<Product> productList = new ArrayList<Product>();
		for(ShoppingCart sc: shoppingCarts){
			if(sc.isChecked()){
				if(sc.isTitle()){
					productList = new ArrayList<Product>();
					shop = new Shop(sc.getShop().getId(), sc.getShop().getName(), productList);
					shopList.add(shop);
				}else{
					if(shop == null || (shop != null && shop.getId() != sc.getShop().getId())){
						productList = new ArrayList<Product>();
						shop = new Shop(sc.getShop().getId(), sc.getShop().getName(), productList);
						shopList.add(shop);
					}

					Product product = new Product(
							sc.getProduct().getProductId(), 
							sc.getProduct().getIcon(), 
							sc.getProduct().getTitle(), 
							sc.getProduct().getPrice(), 
							sc.getProduct().getCount(),
							sc.getProduct().getDescription());
					productList.add(product);
				}
			}
		}
		if(shopList.size() == 0){
			Toast.makeText(getApplicationContext(), "请勾选要支付的商品！", Toast.LENGTH_LONG).show();
			return;
		}
		intent.putExtra("shops", (Serializable)shopList);
		try{
			startActivity(intent);
			finish();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			jumpToSubmitActivity();
			break;
		default:
			break;
		}
	}

	/**
	 * ShoppingCart adapter for listview
	 * @author mfy
	 *
	 */
	public class ShoppingCartAdapter extends ArrayAdapter implements SectionIndexer,OnScrollListener{

		private LayoutInflater inflater;
		private ArrayList<ShoppingCart> shoppingCartList;
		private ShoppingCart shoppingCartSections[];
		public LoadImage loader;

		public ShoppingCartAdapter(Context context, int resource, int textViewResourceId, List objects) {
			super(context, resource, textViewResourceId, objects);
			inflater = LayoutInflater.from(context);
			shoppingCartList = (ArrayList<ShoppingCart>) objects;
			shoppingCartSections = new ShoppingCart[shoppingCartList.size()];

			loader = LoadImage.getInstance();

		}

		public void update(List<ShoppingCart> scList){
			shoppingCartList.clear();
			shoppingCartList.addAll(scList);
			//get section count
			shoppingCartSections = new ShoppingCart[ShoppingCart.getShopCount(shoppingCartList)];
			//copy section
			int i = 0;
			for(ShoppingCart sc : shoppingCartList){
				if(sc.isTitle()){
					shoppingCartSections[i++]=sc;
				}
			}
			this.notifyDataSetChanged();  
		}  

		@Override
		public Object getItem(int position) {
			return shoppingCartList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.view_shopping_cart_item, null);

				holder = new ViewHolder();
				holder.shopLinear = (LinearLayout) convertView.findViewById(R.id.shop_layout);
				holder.shopCB = (CheckBox) convertView.findViewById(R.id.shop_cb);
				holder.shopNameTV =  (TextView) convertView.findViewById(R.id.shop_name_tv);
				holder.amountEdit = (EditText)convertView.findViewById(R.id.product_amount);
				holder.amountEdit.setOnTouchListener(new OnTouchListener(){
					public boolean onTouch(View v, MotionEvent event) {
						holder.amountEdit.setInputType(InputType.TYPE_NULL); // 关闭软键盘      
						if(event.getAction() == MotionEvent.ACTION_UP){
							dialog(position);
						}
						return false;
					}});
				holder.productLinear = (LinearLayout) convertView.findViewById(R.id.product_layout);
				holder.productCB = (CheckBox) convertView.findViewById(R.id.product_price_cb);
				holder.productPriceIV = (ImageView) convertView.findViewById(R.id.product_price_iv);
				holder.productPriceTV = (TextView) convertView.findViewById(R.id.product_price_tv);
				holder.productTitleTV = (TextView) convertView.findViewById(R.id.product_title_tv);
				holder.productDescriptionTV = (TextView) convertView.findViewById(R.id.product_description_tv);
				holder.dividerView = (View)convertView.findViewById(R.id.divider_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			try {
				final ShoppingCart shoppingCart = (ShoppingCart) shoppingCartList.get(position);
				if(shoppingCart.isTitle()){
					holder.shopLinear.setVisibility(View.VISIBLE);
					holder.productLinear.setVisibility(View.GONE);
					holder.shopNameTV.setText(shoppingCart.getShop().getName());
					holder.shopCB.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							for(ShoppingCart sc : shoppingCartList){
								if(sc.getShop().getId() == shoppingCart.getShop().getId()){
									sc.setChecked(sc.isTitle() ? !shoppingCart.isChecked() : shoppingCart.isChecked());
								}
							}
							handler.sendEmptyMessage(FLASH_PRICE);
						}
					});
					holder.shopCB.setChecked(shoppingCart.isChecked());
				}else{
					holder.productLinear.setVisibility(View.VISIBLE);
					holder.shopLinear.setVisibility(View.GONE);
					holder.productTitleTV.setText(shoppingCart.getProduct().getTitle());
					holder.productPriceIV.setImageResource(R.drawable.bacy);
					if (shoppingCart.getProduct().getIcon() != null) {
						holder.productPriceIV.setTag(shoppingCart.getProduct().getIcon());
						loader.addTask(shoppingCart.getProduct().getIcon(),holder.productPriceIV);
						loader.doTask();
					}
					//holder.productPriceTV.setText(htmlParseProductPrice(shoppingCart.getProduct().getPrice(), shoppingCart.getProduct().getCount()));
					holder.productPriceTV.setText(htmlParseProductPrice(shoppingCart.getProduct().getPrice()));
					holder.amountEdit.setText(String.valueOf(shoppingCart.getProduct().getCount()));
					
					holder.productDescriptionTV.setText(shoppingCart.getProduct().getDescription());
					if(shoppingCart.isLastProductOfShop()){
						holder.dividerView.setVisibility(View.GONE);
					}else{
						holder.dividerView.setVisibility(View.VISIBLE);
					}
					holder.productCB.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							for(ShoppingCart sc : shoppingCartList){
								if (!sc.isTitle()
										&& sc.getProduct().getProductId() == shoppingCart
										.getProduct().getProductId()) {
									sc.setChecked(!shoppingCart.isChecked());
								}
							}
							handler.sendEmptyMessage(FLASH_PRICE);
						}
					});
					holder.productCB.setChecked(shoppingCart.isChecked());

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}


		@Override
		public int getCount() {
			return shoppingCartList.size();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			titleLayout(firstVisibleItem);
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getPositionForSection(int section) {
			if(section >= shoppingCartSections.length){
				section = shoppingCartSections.length -1;
			}
			ShoppingCart sc =shoppingCartSections[section];
			int index =  shoppingCartList.indexOf(sc);
			return index;
		}

		@Override
		public int getSectionForPosition(int position) {
			ShoppingCart sc = shoppingCartList.get(position);
			for(int i = 0; i < shoppingCartSections.length; i++){
				if(sc.getShop().getId()==shoppingCartSections[i].getShop().getId()){
					return i;
				}
			}
			return -1;
		}

		@Override
		public Object[] getSections() {
			// TODO Auto-generated method stub
			return shoppingCartSections;
		}

		/*******************   base class and method of ShoppingCartAdapter   *********************/
		class ViewHolder {
			//shop
			LinearLayout shopLinear;
			CheckBox shopCB;
			TextView shopNameTV;

			//product
			LinearLayout productLinear;
			CheckBox productCB;
			ImageView productPriceIV;
			TextView productTitleTV;
			TextView productPriceTV;
			TextView productDescriptionTV;
			EditText amountEdit;
			View dividerView;
		}

		/**
		 * compute state of this title.
		 * @author mfy
		 * @param position
		 * @return
		 */
		public TitleState getTitleState(int position) {
			if (position < 0 || getCount() == 0) {

				return TitleState.TITLE_INVISIBLE;
			}
			int index = getSectionForPosition(position);
			//L.v(TAG, "ShoppingCartActivity position: " + position + "   getSectionForPosition: " + index);
			if (index == -1 || index > shoppingCartSections.length) {

				return TitleState.TITLE_INVISIBLE;
			}
			int section = getSectionForPosition(position);
			int nextSectionPosition = getPositionForSection(section + 1);
			if (nextSectionPosition != -1
					&& position == nextSectionPosition - 1) {

				return TitleState.TITLE_FLOAT;
			}

			return TitleState.TITLE_VISIBLE;
		}

		/**
		 * set title data/linstener
		 * @author mfy
		 * @param firstVisiblePosition
		 */
		public void setTitleText(int firstVisiblePosition) {
			//find view
			final CheckBox shopCB = (CheckBox) floatShopLinear.findViewById(R.id.shop_cb);
			TextView shopNameTV = (TextView) floatShopLinear.findViewById(R.id.shop_name_tv);

			//find fir visible item
			final ShoppingCart shoppingCart = shoppingCartList.get(firstVisiblePosition);  

			//find parent of Product
			ShoppingCart shoppingCartTitle = ShoppingCart.getShoppingCartParent(shoppingCart, shoppingCartList);

			//set data/listener
			shopNameTV.setText(shoppingCart.getShop().getName());
			shopCB.setChecked(shoppingCartTitle.isChecked());
			final ShoppingCart shoppingCartShop = shoppingCartTitle;
			shopCB.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for(ShoppingCart sc : shoppingCartList){
						if(sc.getShop().getId() == shoppingCart.getShop().getId()){
							sc.setChecked(sc.isTitle() ? !shoppingCartShop.isChecked() : shoppingCartShop.isChecked());
						}
					}
					handler.sendEmptyMessage(FLASH_PRICE);
				}
			});
		}  

		/**
		 * flash title UI.
		 * @author mfy
		 * @param firstVisiblePosition
		 */
		public void titleLayout(int firstVisiblePosition) {
			TitleState state = TitleState.TITLE_INVISIBLE;
			state = getTitleState(firstVisiblePosition);
			//L.v(TAG, "TitleListView state: " + state);
			switch(state){
			case TITLE_INVISIBLE:
				floatShopLinear.setVisibility(View.INVISIBLE);
				break;
			case TITLE_VISIBLE:
				floatShopLinear.scrollTo(0, 0);
				floatShopLinear.setVisibility(View.VISIBLE);
				setTitleText(firstVisiblePosition); 
				break;
			case TITLE_FLOAT:
				View firstView=listView.getChildAt(0);
				if(firstView!=null){
					int bottom=firstView.getBottom();
					int headerHeight=floatShopLinear.getHeight();
					int top;
					if(bottom<headerHeight){
						top=(bottom-headerHeight);
					}else{
						top=0;
					}
					floatShopLinear.scrollTo(0, -top);
					setTitleText(firstVisiblePosition);
				}
				
				break;
			}
		}
	}
  //private static int shu = 1;
 private EditText edits;
	protected void dialog(final int position) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.about_dialog,(ViewGroup) findViewById(R.id.dialog));
		Button btnjia = (Button)layout.findViewById(R.id.jia);
		Button btnjian = (Button)layout.findViewById(R.id.jian);
		edits = (EditText)layout.findViewById(R.id.shu_edit);
		edits.setText(String.valueOf(shoppingCarts.get(position).getProduct().getCount()));
		btnjia.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//shu ++;
				int count = shoppingCarts.get(position).getProduct().getCount();
				count++;
				edits.setText(String.valueOf(count));
				shoppingCarts.get(position).getProduct().setCount(count);	
			}});
		btnjian.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				int count = shoppingCarts.get(position).getProduct().getCount();
				edits.setText(String.valueOf(count));
				if(count>0){
					count--;
					edits.setText(String.valueOf(count));
					shoppingCarts.get(position).getProduct().setCount(count);	
				}
				
				
			}});
		new AlertDialog.Builder(this).setTitle("请添加数量").setView(layout)
		.setPositiveButton("确认", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//shu = 1;
				//adapter.notifyDataSetChanged();
				handler.sendEmptyMessage(FLASH_PRICE);
				//holder.amountEdit.setText(String.valueOf(shu));
			}
		})

		.setNegativeButton("取消", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//shu = 1;
			}
		}).show();
		
	}
	/**
	 * make style by html parse.
	 * @param price
	 * @param count
	 * @return
	 */
	private CharSequence htmlParseProductPrice(float price){
		return Html.fromHtml("￥" + "<font color='#333333'>" + price + "</font>"+"x" );
	}
	private CharSequence htmlParseProductPrice1(int count){
		return Html.fromHtml("<font color='#358347'>" + count + "</font>");
	}




}
