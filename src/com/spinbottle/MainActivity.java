package com.spinbottle;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

	protected static final String LIST_KEY = null;
	ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private SimpleAdapter adapter;   
	View alert_view = null;
	int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//把資料加入ArrayList中
		for(int i=0; i<mPeople.length; i++){
			 HashMap<String,Object> item = new HashMap<String,Object>();		 
			 item.put("pic", mPics[i]);
			 item.put("people", mPeople[i]);
			 item.put("arrow", mArrow[0]);
			 list.add(item);
		}
		 
		 //新增SimpleAdapter
		 adapter = new SimpleAdapter( 
		 this, 
		 list,
		 R.layout.activity_main, 
		 new String[] { "pic","people","arrow" },
		 new int[] { R.id.imageView1, R.id.textView1 , R.id.imageView2} );
		 
		 //ListActivity設定adapter
		 setListAdapter( adapter );
		 
		 //啟用按鍵過濾功能，這兩行資料都會進行過濾
		 getListView().setTextFilterEnabled(true);
		 ListView lv = (ListView) this.findViewById(android.R.id.list);
		 lv.setBackgroundResource(R.drawable.bar);
		 lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				 //點選要作的事 
				 showPopUp(position);
			  } 
		});
		
		 
	}
	
	private void showPopUp(int position) {
		//-----------取得alert Layout reference--------
		   LayoutInflater inflater = LayoutInflater.from(MainActivity.this);   
		   final ArrayList<String> spin_list = new ArrayList<String>(); 

		   pos = position;
	       if(position == 0) {
			   alert_view = inflater.inflate(R.layout.alert_two,null);//alert為另外做給alert用的layout
	       }else if(position ==1) {
	    	   alert_view = inflater.inflate(R.layout.alert_three,null);
	       } else {
	    	   alert_view = inflater.inflate(R.layout.alert_four,null);
	       }

	       AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);  
	       helpBuilder.setIcon(R.drawable.racebegin);
	       helpBuilder.setTitle("參賽者名單");  
	       helpBuilder.setView(alert_view);
	       
	       
   
	       //Save button  
	       helpBuilder.setPositiveButton("確認", new DialogInterface.OnClickListener() {  
	    	   public void onClick(DialogInterface dialog, int which) {  
	    		   //Save info here  
	    		   Intent intent = new Intent();

	    		   if(pos == 0) {
	    			   EditText two_edituser1 = (EditText) alert_view.findViewById(R.id.two_edituser1);
	    			   EditText two_edituser2 = (EditText) alert_view.findViewById(R.id.two_edituser2);	    			   
	    			   spin_list.add(two_edituser1.getText().toString());
	    			   spin_list.add(two_edituser2.getText().toString());	    			   
		    		   intent.putStringArrayListExtra(LIST_KEY, spin_list);
	    			   intent.setClass(MainActivity.this,SpinTwoActivity.class);  
	    		   }else if(pos == 1) {
	    			   EditText three_edituser1 = (EditText) alert_view.findViewById(R.id.three_edituser1);
	    			   EditText three_edituser2 = (EditText) alert_view.findViewById(R.id.three_edituser2);
	    			   EditText three_edituser3 = (EditText) alert_view.findViewById(R.id.three_edituser3);
	    			   spin_list.add(three_edituser1.getText().toString());
	    			   spin_list.add(three_edituser2.getText().toString());
	    			   spin_list.add(three_edituser3.getText().toString());
		    		   intent.putStringArrayListExtra(LIST_KEY, spin_list);
	    			   intent.setClass(MainActivity.this,SpinThreeActivity.class);  
	    		   }else {
	    			   EditText four_edituser1 = (EditText) alert_view.findViewById(R.id.four_edituser1);
	    			   EditText four_edituser2 = (EditText) alert_view.findViewById(R.id.four_edituser2);
	    			   EditText four_edituser3 = (EditText) alert_view.findViewById(R.id.four_edituser3);
	    			   EditText four_edituser4 = (EditText) alert_view.findViewById(R.id.four_edituser4);
	    			   spin_list.add(four_edituser1.getText().toString());
	    			   spin_list.add(four_edituser2.getText().toString());
	    			   spin_list.add(four_edituser3.getText().toString());
	    			   spin_list.add(four_edituser4.getText().toString());
		    		   intent.putStringArrayListExtra(LIST_KEY, spin_list);
	    			   intent.setClass(MainActivity.this,SpinFourActivity.class);  
	    		   }
	    		   
	    		   startActivity(intent);

	    	   }  
	       });  
	       //Cancel button  
	       helpBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	       @Override  
	       		public void onClick(DialogInterface dialog, int which) {  
	    	   	// Do nothing, just close the dialog box  
	       		}  
	       });  
	       // Remember, create doesn't show the dialog  
	       AlertDialog helpDialog = helpBuilder.create();  
	       helpDialog.show();  
	     
	}

	 private static final String[] mPeople = new String[] {
	 "兩人", "三人", "四人"
	 };
	 
	 private static final int[] mPics=new int[]{
		 R.drawable.twouser,R.drawable.threeuser,R.drawable.fouruser
		 };
	 private static final int[] mArrow=new int[]{
		 R.drawable.arrow
		 };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            dialog();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void dialog(){
        new AlertDialog.Builder(this)
        .setTitle("提示")
        .setMessage("確定要退出嗎？")
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent home = new Intent(Intent.ACTION_MAIN);  
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
                home.addCategory(Intent.CATEGORY_HOME);  
                startActivity(home);  
            }
        })
        .show();
    }

}
