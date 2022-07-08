package com.group5.sellit.Viewholder;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.R;

import java.util.List;

public class UploadListAdapter  extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList){

        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_upload, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String filename = fileNameList.get(position);
        holder.image_name.setText(filename);

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView image_name;
        public ImageView image_uploaded;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            image_name = (TextView) mView.findViewById(R.id.image_name);
            image_uploaded = (ImageView) mView.findViewById(R.id.image_uploaded);

        }
    }

}
