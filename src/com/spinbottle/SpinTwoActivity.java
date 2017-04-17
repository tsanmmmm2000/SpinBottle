package com.spinbottle;

import com.spinbottle.CustomAnimDrawable.AnimationDrawableListener;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;  
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable; 
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SdCardPath")
public class SpinTwoActivity extends Activity implements OnClickListener{
 
	ArrayList<String> m_arrayList;
	ImageView iv = null;
	ImageView img_user1 = null;
	ImageView img_user2 = null;
	Boolean isRun = true;
	String winner;
	int winner_icon;
	public static int takeType = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spin_two);
				
        iv = (ImageView) findViewById(R.id.imagetwobeer);        
        iv.setOnClickListener(this); 
        
				
		Intent list_intent = getIntent();  
		m_arrayList = list_intent.getExtras().getStringArrayList(MainActivity.LIST_KEY);  
		TextView two_user1 = (TextView)findViewById(R.id.two_user1);
		TextView two_user2 = (TextView)findViewById(R.id.two_user2);
		two_user1.setText(m_arrayList.get(0));
		two_user2.setText(m_arrayList.get(1));
		
		img_user1 = (ImageView) findViewById(R.id.two_captain_america); 
        img_user2 = (ImageView) findViewById(R.id.two_hawkeye); 
        
        
        img_user1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {	
        		iscamera_dialog(1);
        	}

        });        
        img_user2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		iscamera_dialog(2);
        	}

        });
	}

	
	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if(requestCode == 3 || requestCode == 4) {
				ContentResolver resolver = getContentResolver();
				Uri uri = data.getData();
				Bitmap bmp = null;
				try {
					//取得圖片Bitmap
				    bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
				    bmp = getResizedBitmap(bmp,100,100);
					bmp = getCircleBitmap(bmp);
					if(requestCode == 3) {
						((ImageView) findViewById(R.id.two_captain_america)).setImageBitmap(bmp); 
					} else if(requestCode == 4) {
						((ImageView) findViewById(R.id.two_hawkeye)).setImageBitmap(bmp); 
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
				    e.printStackTrace();
				}
			} else {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {   //檢測sd卡是否有用
					Log.v("TestFile","SD card is not avaiable/writeable right now.");
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");  //獲取相機返回的值，並轉為Bitmap格式
				FileOutputStream b = null;
				File file = new File("/sdcard/SpinBottle/");
				file.mkdirs();  //創建資料夾
				String fileName = "/sdcard/SpinBottle/face.jpg";
				try {
					b = new FileOutputStream(fileName);
					if (takeType == 1) {  
	    	            Matrix matrix = new Matrix();  
	    	            matrix.reset();  
	    	            matrix.postRotate(90);  
	    	            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),  
	    	            		bitmap.getHeight(), matrix, true); 
	    	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b); //把值寫入文件
						bitmap = getResizedBitmap(bitmap,167,100);
	    	        } else {
	    	        	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b); //把值寫入文件
						bitmap = getResizedBitmap(bitmap,100,100);
	    	        }
					bitmap = getCircleBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(requestCode == 1) {
					((ImageView) findViewById(R.id.two_captain_america)).setImageBitmap(bitmap);  // 將圖片顯示在ImageView裡
				} else if(requestCode == 2) {
					((ImageView) findViewById(R.id.two_hawkeye)).setImageBitmap(bitmap); 
				}
			}
		}
	}


	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	    return resizedBitmap;
	}
	
	 private  Bitmap getCircleBitmap(Bitmap bitmap) {
	        int x = bitmap.getWidth();
	        Bitmap output = Bitmap.createBitmap(x,
	                x, Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);
	 
	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	        paint.setAntiAlias(true);
	        paint.setColor(color);
	        canvas.drawCircle(x/2, x/2, x/2, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	        return output;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spin_two, menu);
		return true;
	}
	
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub				
    	int head_or_tail = getRandom(0,1);

    	if(head_or_tail == 0) {
    		iv.setBackgroundResource(R.drawable.spin_up);
        	winner = m_arrayList.get(0);
        	winner_icon = R.drawable.captain_america;
        } else {
        	iv.setBackgroundResource(R.drawable.spin_down);
        	winner = m_arrayList.get(1);
        	winner_icon = R.drawable.hawkeye;
        }

	
	    AnimationDrawable anim = null;
	    Object ob = iv.getBackground();
	    anim = (AnimationDrawable) ob;

	    
	    CustomAnimDrawable cusAnim = new CustomAnimDrawable(anim);
	    cusAnim.setAnimationListener(new FrameAnimationListener());  
	    
	    cusAnim.stop();
	    cusAnim.start();
	    	    
	}
	
	class FrameAnimationListener implements AnimationDrawableListener{
		@Override
		public void onAnimationEnd(AnimationDrawable animation) {
			dialog(winner,winner_icon);
		}
		@Override
		public void onAnimationStart(AnimationDrawable animation) {

		}
	}
	
	private int getRandom(int min,int max){
        long seed = System.currentTimeMillis();
        Random r = new Random(); 
        r.setSeed(seed);        
        return (min + r.nextInt(max-min+1));
	}
	

    protected void dialog(String winner,int winner_icon){
        new AlertDialog.Builder(this)
        .setTitle("恭喜中獎！")
        .setMessage(winner)
        .setIcon(winner_icon)
        .setNegativeButton("離開", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent();
            	intent.setClass(SpinTwoActivity.this,MainActivity.class);
            	SpinTwoActivity.this.startActivity(intent); 
            }
        })
        .setPositiveButton("再玩一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //要讓他先取不同資源，不然下次又一樣的話他動畫不會轉
                iv.setBackgroundResource(R.drawable.spin_origin);
            }
        })
        .show();
    }
    
    protected void iscamera_dialog(final int rcode){
        new AlertDialog.Builder(this)
        .setTitle("選擇圖片來源")
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setNeutralButton("從現有圖片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	// 建立 "選擇檔案 Action" 的 Intent
                //Intent intent = new Intent(Intent.ACTION_PICK);
            	Intent intent = new  Intent();
            	intent.setAction(Intent.ACTION_GET_CONTENT);
                // 過濾檔案格式
                intent.setType("image/*"); 
                // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
                Intent destIntent = Intent.createChooser(intent, "選擇檔案");
                // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
                int resource = 0;
                if(rcode == 1) {
                	resource = 3;
                } else if (rcode == 2) {
                	resource = 4;
                }
                startActivityForResult(destIntent, resource);
            }
        })
        .setPositiveButton("馬上拍一張", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        		startActivityForResult(intent, rcode);
            }
        })
        .show();
    }
    
    @Override  
    public void onConfigurationChanged(Configuration newConfig) {  
   	 	Log.d("onConfigurationChanged", "-------onConfigurationChanged----------");
   	 	int type = this.getResources().getConfiguration().orientation;  
   	 	if (type == Configuration.ORIENTATION_LANDSCAPE) {   
   	 		Log.d("onConfigurationChanged", "------Landscape----------");
   	 		takeType = 0;
   	 	} else if (type == Configuration.ORIENTATION_PORTRAIT) {  
   	 		Log.d("onConfigurationChanged", "------Portrait----------");
   	 		takeType = 1;
   	 	}
   	 	super.onConfigurationChanged(newConfig);  
    } 

}
