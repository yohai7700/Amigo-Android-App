package com.example.amigo.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amigo.Adapter.GroupAdapter;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;
import com.example.amigo.StatsViewModel.ViewModel.GroupViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This activity displays the list of groups.
 * @author Yohai Mazuz
 */
public class GroupListActivity extends AppCompatActivity {

    private RecyclerView groupListRecyclerView;
    private GroupAdapter groupAdapter;
    private FloatingActionButton buttonAddGroup;
    private GroupViewModel groupViewModel;
    private boolean shouldAskOnDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        setTitle("Groups List");
        setButtonAddGroup();
        setGroupAdapter();
        setGroupsRecyclerView();
        setGroupViewModel();
        setSwipeHelper();
        setAdapterOnLongClickListener();
        setAdapterOnClickListener();
    }

    /**
     * Sets the on click which opens the group
     */
    private void setAdapterOnClickListener(){
        groupAdapter.setOnClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Group group) {
                Intent intent = new Intent(GroupListActivity.this, GroupStandingsActivity.class);
                intent.putExtra(GroupStandingsActivity.EXTRA_GROUP_ID, group.id);
                intent.putExtra(GroupStandingsActivity.EXTRA_GROUP_TITLE, group.getTitle());
                startActivity(intent);
            }
        });
    }
    /**
     * Sets the on click which edits the group
     */
    private void setAdapterOnLongClickListener(){
        groupAdapter.setOnLongClickListener(new GroupAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Group group) {
                Intent intent = new Intent(GroupListActivity.this, AddEditGroupActivity.class);
                intent.putExtra(AddEditGroupActivity.EXTRA_GROUP_ID, group.id);
                startActivity(intent);
            }
        });
    }

    /**
     * Sets recycler view that shows the groups
     */
    private void setGroupsRecyclerView(){
        groupListRecyclerView = findViewById(R.id.group_list_recycler_view);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupListRecyclerView.setHasFixedSize(true);
        groupListRecyclerView.setAdapter(groupAdapter);
    }

    /**
     * Sets group adapter
     */
    private void setGroupAdapter(){
        groupAdapter = new GroupAdapter();
        groupAdapter.setContext(this);
    }

    /**
     * Sets view model for groups
     */
    private void setGroupViewModel(){
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        LiveData<List<Group>> groups = groupViewModel.getAllGroups();
        groupViewModel.getAllGroups().observe(this, new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                groupAdapter.submitList(groups);
            }
        });
    }

    /**
     * Sets button that adds a new group
     */
    private void setButtonAddGroup(){
        buttonAddGroup = findViewById(R.id.add_group_button);
        buttonAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this, AddEditGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Sets swipe helper that shows a dialog for asking to delete a group
     */
    private void setSwipeHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if(shouldAskOnDelete) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    groupViewModel.delete(groupAdapter.getGroupAt(viewHolder.getAdapterPosition()));
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    groupAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    break;
                            }
                        }
                    };

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(GroupListActivity.this);
                    String message = "Are you sure you want to delete the group " + groupAdapter.getGroupAt(viewHolder.getAdapterPosition()).getTitle() + "?";
                    builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .show();
                }
                else
                    groupViewModel.delete(groupAdapter.getGroupAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(groupListRecyclerView);
    }
}
