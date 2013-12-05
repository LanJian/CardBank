package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jackhxs.util.ImageUtil;

public class TemplateGallery extends Activity {
    private String selectedImagePath;
    private static final int SELECT_PICTURE = 1;

	public static int getStringIdentifier(Context context, String name) {
	    return context.getResources().getIdentifier(name, "string", context.getPackageName());
	}

	public class ImageAdapter extends BaseAdapter {
		private final String sampleName = getString(R.string.your_name);
		private final String sampleEmail = getString(R.string.your_email);
		private final String sampleNumber = getString(R.string.your_number);
		
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(768, 416));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			Bitmap newCard = ImageUtil.GenerateCardImage((Activity) mContext, App.templateConfig[0], sampleName, sampleEmail, sampleNumber);
			imageView.setImageBitmap(newCard);

			return imageView;
		}

		// references to our images
		// Order is important
		private Integer[] mThumbIds = {
			R.drawable.template_green,
			R.drawable.template_gold,
			R.drawable.template_colorful,
			R.drawable.template_circle,
			R.drawable.template_bar
		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final Activity selfRef = this;
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gallery);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Log.e("Clicked me", String.valueOf(position));
				Intent intent = new Intent(getApplicationContext(),
						CardEditActivity.class);
				intent.putExtra("templateIndex", position);
				startActivity(intent);
				selfRef.finish();
				  // in onCreate or any event where your want the user to
                // select a file
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        "Select Picture"), SELECT_PICTURE);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
}