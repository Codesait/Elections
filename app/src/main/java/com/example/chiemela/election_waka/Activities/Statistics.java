package com.example.chiemela.election_waka.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chiemela.election_waka.Model.VideoModel;
import com.example.chiemela.election_waka.Model.CamModel;
import com.example.chiemela.election_waka.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Statistics extends AppCompatActivity {

    private Button btnRetrieve;
    private TextView tvAfforestEconomics;
    private TextView tvAfforestNonEco;

    private TextView tvPrimary;
    private TextView tvGeneral;
    private DatabaseReference treeRef;
    private DatabaseReference defRef;
    private String noEcoAffoTree;
    private String noNEcoAffoTree;
    private String noEcoDefTree;
    private String noNonEcoDefTree;
    private String electionType;
    private String electionTypeDef;
    private int treenoEco;
    private int treenoNonEco;
    private int ElectionNo;
    private int defTreeNoNonEco;
    private Button display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

//        tvAfforestEconomics = findViewById(R.id.tvNoOfTrees);
////        tvAfforestNonEco =findViewById(R.id.tvNoOfTreesN);
        display = findViewById(R.id.displayStatistics);
        tvPrimary = findViewById(R.id.tvNoOfPrimaryElectionReported);
        tvGeneral = findViewById(R.id.tvNoOfGeneralReported);


        treeRef= FirebaseDatabase.getInstance().getReference().child("Election-vid-report");
        defRef = FirebaseDatabase.getInstance().getReference().child("Election-cam-report");

        treeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                treenoEco=0;
                treenoNonEco=0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CamModel model = ds.getValue(CamModel.class);
                    electionType = model.getElecionType();
                    Log.d("electionType",""+electionType);

                    if (electionType.matches("Primary")){
                        noEcoAffoTree =model.getHelpReports();
                        //Log.d("ecotree",""+noEcoAffoTree);
                        treenoEco=treenoEco+Integer.parseInt(noEcoAffoTree);
                    }else  if (electionType.matches("General")){
                        noNEcoAffoTree =model.getHelpReports();
                        treenoNonEco=treenoNonEco+Integer.parseInt(noNEcoAffoTree);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        defRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    VideoModel defmodel = ds.getValue(VideoModel.class);
//                    electionTypeDef = defmodel.getTypeOfElection();
//                    Log.d("electionType",""+ electionType);
//
//                    if (electionTypeDef.matches("Primary")){
//                        noEcoDefTree =defmodel.getNoOfElection();
//                        Log.d("ecotree",""+noEcoDefTree);
//                        ElectionNo = ElectionNo +Integer.parseInt(noEcoDefTree);
//                    }else  if (electionTypeDef.matches("General")){
//                        noNonEcoDefTree =defmodel.getNoOfElection();
//                        defTreeNoNonEco=defTreeNoNonEco+Integer.parseInt(noNonEcoDefTree);
//                    }
//
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Report",""+treenoEco);
//                tvAfforestEconomics.setText(""+treenoEco);
//                tvAfforestNonEco.setText(""+treenoNonEco);
                tvPrimary.setText(""+ ElectionNo);
                tvGeneral.setText(""+defTreeNoNonEco);

            }
        });
    }

}
