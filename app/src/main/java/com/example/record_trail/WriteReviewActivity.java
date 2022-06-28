package com.example.record_trail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteReviewActivity extends Activity {

    static final int PICK_FROM_CAMERA = 0;
    static final int PICK_FROM_ALBUM = 1;
    static final int CROP_FROM_iMAGE = 2;
    Uri mImageCaptureUri;
    int id_view;
    String absoultePath;

    SQLiteDatabase db;
    DBHelper_review helper;

    ImageView imgV;
    Button saveBtn;
    ImageButton uploadBtn, backBtn;
    TextView text;
    DateFormat save_df = new SimpleDateFormat("yyyy-MM-dd");

    byte[] bytes = new byte[]{};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        text = findViewById(R.id.et_review);
        imgV = findViewById(R.id.Image_map);
        uploadBtn = findViewById(R.id.UploadIMG);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.back_button);

        // DB
        helper = new DBHelper_review(this, "review.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTakeAlbumAction();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_data();
            }
        });
    }

    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE);
                break;
            }
            case CROP_FROM_iMAGE: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";
                if (extras != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap photo = extras.getParcelable("data");
                    imgV.setImageBitmap(photo);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);    //수정
                    bytes = stream.toByteArray();
                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);
        if (!directory_SmartWheel.exists()) directory_SmartWheel.mkdir();
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save_data() {
        Calendar cal = Calendar.getInstance();
        String save_date = save_df.format(cal.getTime());
        Log.i("테스트", String.valueOf(bytes.length));
        db.execSQL("insert into review('date', 'content', 'image') values('" + save_date + "', '" + text.getText() + "', '" + bytes + "');");

    }
}
