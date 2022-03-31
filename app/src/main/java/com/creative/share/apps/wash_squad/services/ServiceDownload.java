package com.creative.share.apps.wash_squad.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;


import com.creative.share.apps.wash_squad.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ServiceDownload extends Service{

    private String file_url = "";
    private String file_name = "";
    private String file_local_path = "";

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, R.string.downloading2, Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            file_url = intent.getStringExtra("file_url");
            file_name = intent.getStringExtra("file_name");
            new DownLoadTask().execute(file_url);
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class DownLoadTask extends AsyncTask<String, String, String> {

        private int count = 0;
        private int progress = 0;
        private NotificationCompat.Builder builder;
        private NotificationManager manager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                initNewVersionNotification(0);
            } else {
                initOldVersionNotification(0);
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                String extension  = file_url.substring(file_url.lastIndexOf("."));

                File outFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Ebsar_App");

                if (!outFile.exists()) {
                    outFile.mkdir();
                }

                file_local_path = outFile.getAbsolutePath() + "/" + file_name.replaceAll(" ","_")+extension;

                File finalFile = new File(file_local_path);

                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int length = connection.getContentLength();

                byte[] data;

                if (length > 0) {
                    data = new byte[length];
                } else {
                    length = 1024;
                    data = new byte[1024];
                }
                long total = 0;


                InputStream inputStream = connection.getInputStream();
                OutputStream outputStream = new FileOutputStream(finalFile);

                while ((count = inputStream.read(data)) != -1) {

                    total += count;
                    progress = (int) ((total * 100) / length);
                    publishProgress(progress + "");
                    outputStream.write(data, 0, count);
                }
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {

            manager.cancel("ebsar_tag", 1585);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                initNewVersionNotification(1);
            } else {
                initOldVersionNotification(1);
            }
            stopSelf();
            Toast.makeText(ServiceDownload.this, R.string.down_done, Toast.LENGTH_SHORT).show();


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            builder.setProgress(100, Integer.parseInt(values[0]), false);
            manager.notify("ebsar_tag", 1585, builder.build());

        }

        private void initOldVersionNotification(int type) {
            builder = new NotificationCompat.Builder(getApplicationContext());
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            if (type == 0) {
                builder.setSmallIcon(R.drawable.ic_download);

                builder.setContentTitle(getString(R.string.download));

                builder.setProgress(100, 0, false);
            } else {
                builder.setSmallIcon(R.drawable.ic_done);

                builder.setContentTitle(getString(R.string.down_done));

            }
            builder.setContentText(file_name);
            builder.setSound(null);
            builder.setDefaults(0);
            builder.setWhen(System.currentTimeMillis());

            manager.notify("ebsar_tag", 1585, builder.build());


        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void initNewVersionNotification(int type) {
            String channel_id = "ebsar_channel_id";
            CharSequence channel_name = "ebsar_channel_name";
            int Importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, Importance);


            builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
            builder.setChannelId(channel_id);

            Intent intent = new Intent();


            if (type == 0) {
                builder.setSmallIcon(R.drawable.ic_download);

                builder.setContentTitle(getString(R.string.download));

                builder.setProgress(100, 0, false);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);


            } else {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(file_local_path)));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntent(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setSmallIcon(R.drawable.ic_done);

                builder.setContentTitle(getString(R.string.down_done));

            }


            builder.setContentText(file_name);
            builder.setSound(null);
            builder.setDefaults(0);
            channel.setSound(null, null);
            manager.createNotificationChannel(channel);
            manager.notify("ebsar_tag", 1585, builder.build());

        }


    }
}
