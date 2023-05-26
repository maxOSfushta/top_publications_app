package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RedditPostViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView;
    private final TextView authorTextView;
    private final TextView commentsTextView;
    ImageView thumbnailImageView;

    public RedditPostViewHolder(@NonNull View itemView, List<RedditPost> posts) {
        super(itemView);
        thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
        titleTextView = itemView.findViewById(R.id.titleTextView);
        authorTextView = itemView.findViewById(R.id.authorTextView);
        commentsTextView = itemView.findViewById(R.id.commentsTextView);

        thumbnailImageView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                RedditPost post = posts.get(position);
                String originalUrl = post.getImageUrl();
                openOriginalUrl(originalUrl);
            }
        });

        thumbnailImageView.setOnLongClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                RedditPost post = posts.get(position);
                String imageUrl = post.getImageUrl();
                saveImageToGallery(itemView.getContext(), imageUrl);
                return true;
            }
            return false;
        });
    }

    @SuppressLint("SetTextI18n")
    public void bind(RedditPost post) {
        itemView.setTag(post);

        titleTextView.setText(post.getTitle());
        authorTextView.setText("Posted by " + post.getAuthor() + " • " + getFormattedTimeAgo(post.getCreatedUtc()));
        commentsTextView.setText("Comments: " + post.getNumComments());

        if (post.getThumbnailUrl() != null && !post.getThumbnailUrl().isEmpty()) {
            Picasso.get().load(post.getThumbnailUrl()).into(thumbnailImageView);
            thumbnailImageView.setVisibility(View.VISIBLE);
        } else {
            thumbnailImageView.setImageDrawable(null);
            thumbnailImageView.setVisibility(View.GONE);
        }
    }

    private void openOriginalUrl(String originalUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(originalUrl));
        itemView.getContext().startActivity(intent);
    }

    private void saveImageToGallery(Context context, String imageUrl) {
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis());
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                } else {
                    File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    contentValues.put(MediaStore.Images.Media.DATA, imageDirectory.getAbsolutePath());
                }

                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                try {
                    OutputStream outputStream = resolver.openOutputStream(imageUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();

                    Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private String getFormattedTimeAgo(long createdUtc) {
        long currentTime = System.currentTimeMillis() / 1000L;
        long timeDifference = currentTime - createdUtc;

        if (timeDifference < 60) {
            return timeDifference + " seconds ago";
        } else if (timeDifference < 3600) {
            long minutes = timeDifference / 60;
            return minutes + " minutes ago";
        } else if (timeDifference < 86400) {
            long hours = timeDifference / 3600;
            if (hours == 1) {
                return "1 hour ago";
            } else {
                return hours + " hours ago";
            }
        } else {
            long days = timeDifference / 86400;
            if (days == 1) {
                return "1 day ago";
            } else {
                return days + " days ago";
            }
        }
    }
}