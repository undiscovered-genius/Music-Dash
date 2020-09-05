package com.example.musictab;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusic extends Fragment {
    ListView allmusiclist;
    ArrayAdapter<String> musicarrayadapter;
    String song[];
    ArrayList<File> musics;

    public AllMusic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_music, container, false);

        allmusiclist = view.findViewById(R.id.musiclist);

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                musics = findMusicFile(Environment.getExternalStorageDirectory());
                song = new String[musics.size()];
                for (int i =0; i<musics.size();i++){
                    song[i]=musics.get(i).getName();
                }
                musicarrayadapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,song);
                allmusiclist.setAdapter(musicarrayadapter);

                //song click
                allmusiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent player = new Intent(getActivity(),Player.class);
                        player.putExtra("songFileList",musics);
                        player.putExtra("position",position);

                        startActivity(player);

                    }
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

        return view;
    }
    private ArrayList<File> findMusicFile(File file){
        ArrayList<File> allMusicFileObject = new ArrayList<>();
        File [] files = file.listFiles();

        for (File currentFile: files){
            if (currentFile.isDirectory() && !currentFile.isHidden()){
                allMusicFileObject.addAll(findMusicFile(currentFile));
            }
            else{
                if (currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".mp4a") || currentFile.getName().endsWith(".wav") || currentFile.getName().endsWith(".mp4")) {
                    allMusicFileObject.add(currentFile);
                }
            }
        }
        return allMusicFileObject;
    }

}
