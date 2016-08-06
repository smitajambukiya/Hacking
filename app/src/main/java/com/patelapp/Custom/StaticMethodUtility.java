package com.patelapp.Custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.patelapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Android on 1/10/2016.
 */
public class StaticMethodUtility {

    static ProgressDialog mProgressDialog = null;

    public final static String internal_storage_path = Environment.getDataDirectory() + "/data/data/" + GlobalData.PACKAGE_NAME + "/" + GlobalData.APP_NAME;

    public final static String sd_card_path = Environment.getExternalStorageDirectory() + "/" + GlobalData.APP_NAME;

    public static String testUserID = "3";

    @SuppressLint("NewApi")
    public static int[] getDeviceWidthHeight(Activity context) {
        int[] asd = {0, 0}; //Width and Height
        Point size = new Point();
        WindowManager w = context.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            asd[0] = size.x;
            asd[1] = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            asd[0] = d.getWidth();
            asd[1] = d.getHeight();
            ;
        }
        return asd;
    }

    public static void showProgressDialog(Activity context, String message) {
        try {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message + "...");
            mProgressDialog.getWindow().setGravity(Gravity.CENTER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNetworkToast(Context context){
        Toast.makeText(context,context.getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
    }
    public static void showProgressDialogByContext(Context context, String message) {
        try {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message + "...");
            mProgressDialog.getWindow().setGravity(Gravity.CENTER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowingProgressDialog() {
        try {
            if (mProgressDialog != null)
                return mProgressDialog.isShowing();
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setProgressMessage(String msg) {
        try {
            if (mProgressDialog != null)
                mProgressDialog.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String writeBitmapToFile(Bitmap imageData) {
        String image_path = "";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/GoDelivr");
        myDir.mkdirs();
        String fname = "go_deliver" + ".png";
        File file = new File(myDir, fname);
        image_path = file.getAbsolutePath();
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageData.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return image_path;
    }


    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void rotateImage(String filename) {
        try {
            ExifInterface exif = new ExifInterface(filename);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate -= 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate -= 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate -= 90;
                    break;
            }
            Log.d("Fragment", "EXIF info for file " + filename + ": " + rotate);
        } catch (IOException e) {
            Log.d("Fragment", "Could not get EXIF info for file " + filename + ": " + e);
        }
    }

    public final static boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return (email.matches(EMAIL_PATTERN));
        //return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidMobileNumber(String number) {
        String Phone_Pattern = "^0[0-8]\\d{8}$";

        return (number.matches(Phone_Pattern));
    }

    /*public static List<Integer> getFooterBanner() {
        List<Integer> imageIdList = imageIdList = new ArrayList<Integer>();
        imageIdList.add(R.drawable.banner1);
        imageIdList.add(R.drawable.banner2);
        imageIdList.add(R.drawable.banner3);
        imageIdList.add(R.drawable.banner1);
        imageIdList.add(R.drawable.newlbanner);

        return imageIdList;
    }*/

   /* public static void showPositiveToast(Context context, String msg)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast, null);

        TextView txt = (TextView) layout.findViewById(R.id.tvToast);
        txt.setText(msg);
        txt.setBackgroundResource(R.drawable.toast_green);

        Toast toast = newlbanner Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }*/

    /*public static void showNegativeToast(Activity activity, String msg)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

        TextView txt = (TextView) layout.findViewById(R.id.tvToast);
        txt.setText(msg);
        txt.setBackgroundResource(R.drawable.toast_red);

        Toast toast = newlbanner Toast(activity);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }*/

    /*public static void showNetworkToast(Activity context)
    {
        showNegativeToast(context,context.getResources().getString(R.string.no_network));
    }*/

    public static boolean validateImageFileSizeToCrop(Activity actvity, String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

//            int minPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, actvity.getResources().getDimension(R.dimen.big_image_opaq_size) , actvity.getResources().getDisplayMetrics());
//            LogM.i("Min Pixels " + minPixel);
            if (options.outWidth >= 250 && options.outHeight >= 250) {
                Log.i("tag", options.outHeight + " X " + options.outWidth);
                return true;
            } else {
                Log.i("tag", options.outHeight + " X " + options.outWidth);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static synchronized File getResizeImage(String path) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b = BitmapFactory.decodeFile(path);
        Bitmap out = Bitmap.createScaledBitmap(b, 250, 250, false);
        File file = new File(dir, "resize_profile.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if(heightRatio > 1 || widthRatio > 1)
        {
            if(heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            }
            else
            {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
    public static String getFilePathFromSDCARD(Bitmap image, String file_name) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/OneSocial");
        myDir.mkdirs();
//		String fname = "temp.jpg";
        File file = new File(myDir, file_name);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//		String fileName = "temp.jpg";
        File f = new File(Environment.getExternalStorageDirectory(), "/OneSocial");
        f = new File(f, file_name);

        return f.getAbsolutePath();
    }

    public static boolean deleteDirectory(String FolderName) {
        File path = new File(Environment.getExternalStorageDirectory(), "/" + FolderName);
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
//	           deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    public static void shareBitmap (Context context,Bitmap bitmap) {
        try {
            String pathofBmp = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);

            Uri bmpUri = Uri.parse(pathofBmp);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            intent.setType("image/png");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   public static String getDisplayDate(String your_date){
       String inputPattern = "yyyy-MM-dd";
       //String outputPattern = "dd-MMM-yyyy h:mm a";
       String outputPattern = "dd-MM-yyyy";
       SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
       SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

       Date date = null;
       String str = null;

       try {
           date = inputFormat.parse(your_date);
           str = outputFormat.format(date);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return str;

   }


  /*  public static String getDisplayFullDate(String your_date){
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "dd-MMM-yyyy hh:mm a";
        //String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(your_date);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }*/

    public static Bitmap getScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static int dpToPx(Context context,int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
