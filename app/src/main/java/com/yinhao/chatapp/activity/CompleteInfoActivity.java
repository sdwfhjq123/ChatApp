package com.yinhao.chatapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.PictureUtils;
import com.yinhao.chatapp.utils.Prefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 输入昵称时，弹出Dialog
 * file如果为空不能上传
 */
public class CompleteInfoActivity extends AppCompatActivity {
    private static final String TAG = "CompleteInfoActivity";

    private static final int REQUEST_OPEN_ALBUM = 1;
    private static final int REQUEST_OPEN_CAMERA = 2;
    private static final int REQUEST_GET_ALBUM = 3;
    private static final int REQUEST_GET_CAMERA = 4;


    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CompleteInfoActivity.class);
        return intent;
    }

    private Toolbar mToolbar;
    private LinearLayout mCompileHead;
    private ImageView mHeadImage;
    private LinearLayout mCompileName;
    private TextView mNameText;

    private ProgressBar mProgressBar;


    private File mPhotoFile;
    private boolean mIsNeverCompile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        mPhotoFile = new File(getCacheDir(), "head_image.jpg");

        initToolbar();

        mCompileHead = (LinearLayout) findViewById(R.id.compile_head_linearlayout);
        mCompileHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开popup选择拍照or相册
                showPopupWindow();
            }
        });

        //头像imageview
        mHeadImage = (ImageView) findViewById(R.id.head_image);
        //进度圈
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //编辑昵称
        mCompileName = (LinearLayout) findViewById(R.id.compile_name_linearlayout);
        mNameText = (TextView) findViewById(R.id.name_text);
        final View dialogView = View.inflate(CompleteInfoActivity.this, R.layout.dialog_et, null);
        final EditText dialogEditText = (EditText) dialogView.findViewById(R.id.dialog_edit_text);
        mCompileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompleteInfoActivity.this);
                builder.setTitle("设置昵称");
                builder.setView(dialogView);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNameText.setText("");
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //将输入框获取到的昵称赋值给nameText
                        mNameText.setText(dialogEditText.getText().toString());
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_OPEN_ALBUM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(CompleteInfoActivity.this, "选择相册权限已关闭，请手动从 设置->应用管理 里开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_OPEN_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(CompleteInfoActivity.this, "调用权限已关闭，请手动从 设置->应用管理 里开启", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GET_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case REQUEST_GET_CAMERA:
                try {
                    //Uri uri = data.getData();
                    Uri uri = FileProvider.getUriForFile(this, "com.yinhao.chatapp.FileProvider", mPhotoFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    savePicture(bitmap);
                    mHeadImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void displayImage(String path) {
        Bitmap bm = PictureUtils.getScaledBitmap(path, this);
        mHeadImage.setImageBitmap(bm);
        savePicture(bm);
    }

    /**
     * 保存到缓存目录
     *
     * @param bitmap
     */
    private File savePicture(Bitmap bitmap) {
        File cacheDir = getCacheDir();
        File file = null;
        FileOutputStream fos = null;
        try {
            file = File.createTempFile(
                    "head_image",
                    ".jpg",
                    cacheDir
            );
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Prefs.putString(this, Prefs.PREF_FILE_PATH, file.getName());
        Log.i(TAG, "filepath1:" + file.getPath());
        return file;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        //Log.i("uri", uri + "");
        if (DocumentsContract.isDocumentUri(CompleteInfoActivity.this, uri)) {
            //如果是document类型的uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，就用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    /**
     * 点击显示选择本地相册或者拍照
     */
    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.popup_choose_pic, null);
        Button btnPopAlbum = (Button) popView.findViewById(R.id.btn_pop_album);
        Button btnPopCamera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button btnPopCancel = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels / 3;
        final PopupWindow popupWindow = new PopupWindow(popView, widthPixels, heightPixels);
        popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击popup外部消失
        popupWindow.setOutsideTouchable(true);
        //消失时屏幕变为半透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        //出现时屏幕变为透明
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);
        btnPopAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (ContextCompat.checkSelfPermission(CompleteInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CompleteInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_OPEN_ALBUM);
                } else {
                    openAlbum();
                }
            }
        });
        btnPopCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (ContextCompat.checkSelfPermission(CompleteInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CompleteInfoActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_OPEN_CAMERA);
                } else {
                    openCamera();
                }
            }
        });
        btnPopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        Uri imageUri = null;
        //创建File对象，用于存储拍照后的照片
        File outputImage = new File(getCacheDir(), "head_image.jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.yinhao.chatapp.FileProvider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(captureImage, REQUEST_GET_CAMERA);
    }

    /**
     * 打开相册
     */

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GET_ALBUM);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteInfoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        //提交完善资料
        mToolbar.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = Prefs.getString(CompleteInfoActivity.this, Prefs.PREF_FILE_PATH);
                //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File storageDir = getCacheDir();
                final File file = new File(storageDir, fileName);
                mProgressBar.setVisibility(View.VISIBLE);
                if ((file.exists()) && (!TextUtils.isEmpty(mNameText.getText().toString()))) {
                    HttpUtils.handleCompleteInfoOnServer(file,
                            "/user/modify",
                            mNameText.getText().toString(),
                            Prefs.getString(CompleteInfoActivity.this, Prefs.PREF_KEY_LOGIN_ID)
                            , new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e(TAG, "上传失败");
                                    mProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String result = response.body().string();
                                    Log.i(TAG, "上传成功:" + result);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressBar.setVisibility(View.GONE);
                                            Toast.makeText(CompleteInfoActivity.this, "完善资料成功，请登录账号", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                }
                            });
                } else {
                    Toast.makeText(CompleteInfoActivity.this, "头像或昵称未完善,请重新编辑", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
