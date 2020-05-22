package com.app.hardik.studypdf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.System.exit;


public class MyAdapter extends MultiLevelAdapter {
    private Holder mViewHolder;
    private Context mContext;
    private List<Item> mListItems = new ArrayList<>();
    private Item mItem;
    private MultiLevelRecyclerView mMultiLevelRecyclerView;
    private FirebaseDatabase db;
    private DatabaseReference dbrefer;
    private Context alertar;


    // Set an EditText view to get user input


    MyAdapter(Context mContext, List<Item> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView) {
        super(mListItems);
        this.mListItems = mListItems;
        this.mContext = mContext;
        this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
    }

    private void setExpandButton(ImageView expandButton, boolean isExpanded) {
        // set the icon based on the current state
        Log.i("Expanded",String.valueOf(isExpanded));
        expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_down_black_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mViewHolder = (Holder) holder;
        mItem = mListItems.get(position);

        if(Apple.INSTANCE.getUpdateClicked() == 1) {
            switch (getItemViewType(position)) {
                case 0:
                    holder.itemView.setBackgroundColor(Color.parseColor("#dbf3fa"));
                    break;
                case 1:
                    holder.itemView.setBackgroundColor(Color.parseColor("#b7e9f7"));
                    break;
                case 2:
                    holder.itemView.setBackgroundColor(Color.parseColor("#93dff3"));
                    break;
                case 3:
                    holder.itemView.setBackgroundColor(Color.parseColor("#7AD7F0"));
                    break;
                default:
                    holder.itemView.setBackgroundColor(Color.parseColor("#f5fcff"));
                    break;
            }

        }
        else if (Apple.INSTANCE.getUpdateClicked() == 0){
            switch (getItemViewType(position)) {
                case 0:
                    holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 1:
                    holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
                    break;
                case 2:
                    holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                    break;
                case 3:
                    holder.itemView.setBackgroundColor(Color.parseColor("#cdcdcd"));
                    break;
                default:
                    holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
            }
        }
        else if (Apple.INSTANCE.getUpdateClicked() == 2){
            switch (getItemViewType(position)) {
                case 0:
                    holder.itemView.setBackgroundColor(Color.parseColor("#F6BDC0"));
                    break;
                case 1:
                    holder.itemView.setBackgroundColor(Color.parseColor("#F1959B"));
                    break;
                case 2:
                    holder.itemView.setBackgroundColor(Color.parseColor("#F07470"));
                    break;
                case 3:
                    holder.itemView.setBackgroundColor(Color.parseColor("#EA4C46"));
                    break;
                default:
                    holder.itemView.setBackgroundColor(Color.parseColor("#F6BDC0"));
                    break;
            }
        }

        mViewHolder.mTitle.setText(mItem.getText());
        mViewHolder.mSubtitle.setText(mItem.getSecondText());

        if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
            setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
            mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mExpandButton.setVisibility(View.GONE);
        }

        Log.e("MuditLog",mItem.getLevel()+" "+mItem.getPosition()+" "+mItem.isExpanded()+"");

        // indent child items
        // Note: the parent item should start at zero to have no indentation
        // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
        float density = mContext.getResources().getDisplayMetrics().density;
        ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
    }

    private class Holder extends RecyclerView.ViewHolder {

        TextView mTitle, mSubtitle;
        ImageView mExpandIcon;
        LinearLayout mTextBox, mExpandButton;

        Holder(final View itemView) {
            super(itemView);
            mTitle =  itemView.findViewById(R.id.title);
            mSubtitle =  itemView.findViewById(R.id.subtitle);
            mExpandIcon =  itemView.findViewById(R.id.image_view);
            mTextBox =  itemView.findViewById(R.id.text_box);
            mExpandButton =  itemView.findViewById(R.id.expand_field);

            // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
            // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here
                   // Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                   if(Apple.INSTANCE.getUpdateClicked() == 1){
                       if(mListItems.get(getAdapterPosition()).getText().equals("New +")){
                           Intent in = new Intent(v.getRootView().getContext(),AddIntoList.class);
                           in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           v.getRootView().getContext().startActivity(in);
                       }
                   }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListItems.get(getAdapterPosition()).getText().equals("New +")|| mListItems.get(getAdapterPosition()).getText().equals("All Available Categories")){
                        return false;
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                    int level = mListItems.get(getAdapterPosition()).getLevel();
                    String currentName = mListItems.get(getAdapterPosition()).text;
                    String path = "",parent2Name,parent1Name,parent0Name = "";
                    Integer parent2,parent1,parent0 = 0;
                    final ListFragment listFragment = new ListFragment();
                    if (level == 3) {
                        parent2 = parentgive(level, 1);
                        parent2Name = mListItems.get(getAdapterPosition() - parent2).text;
                        parent1 = parentgive(2, parent2);
                        parent1Name = mListItems.get(getAdapterPosition() - parent1).text;
                        parent0 = parentgive(1, parent1);
                        parent0Name = mListItems.get(getAdapterPosition() - parent0).text;
                        path = "StreamList/" + parent0Name + "/" + parent1Name + "/" + parent2Name + "/" + currentName;

                    } else if (level == 2) {
                        parent1 = parentgive(level, 1);
                        parent1Name = mListItems.get(getAdapterPosition() - parent1).text;
                        parent0 = parentgive(1, parent1);
                        parent0Name = mListItems.get(getAdapterPosition() - parent0).text;
                        path = "StreamList/" + parent0Name + "/" + parent1Name + "/" + currentName;
                    } else if (level == 1) {
                        parent0 = parentgive(level, 1);
                        parent0Name = mListItems.get(getAdapterPosition() - parent0).text;
                        path = "StreamList/" + parent0Name + "/" + currentName;
                    } else if (level == 0) {
                        path = "StreamList/" + currentName;
                    }
                    final String finalPath = path;
                    if(Apple.INSTANCE.getUpdateClicked() == 2) {
                        //Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was LONG clicked!", getAdapterPosition()), Toast.LENGTH_LONG).show();

                        alert.setTitle("Do you want to Delete?");
                        alert.setMessage("Deleting this will also delete its descendants!");
                        Log.i("path", path);


                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db = FirebaseDatabase.getInstance();
                                dbrefer = db.getReference(finalPath);
                                Log.i("finall delete",finalPath);
                                dbrefer.setValue(null);
                                Toast.makeText(mContext,"Element Deleted successfully,Swipe Down to Refresh",Toast.LENGTH_LONG).show();
                              //  listFragment.reload();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });
                        alert.show();



                    }
                    else if (Apple.INSTANCE.getUpdateClicked() == 1){

                        final String finalPath2 = path+"/";
                        alert.setTitle("Adding new element...");
                        alert.setMessage("New element will be added under selected element!");
                        final EditText input = new EditText(v.getRootView().getContext());
                        alert.setView(input);
                        input.setText(finalPath2);
                        Selection.setSelection(input.getText(),input.getText().length());
                        input.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if(!s.toString().startsWith(finalPath2)){
                                    input.setText(finalPath2);
                                    Selection.setSelection(input.getText(),input.getText().length());

                                }

                            }
                        });

                        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db = FirebaseDatabase.getInstance();
                                dbrefer = db.getReference();
                                String adder = input.getText().toString().trim();
                                Log.i("finall 3",finalPath2 + " "+adder.trim());
                                if(adder.equals(finalPath2)){
                                    Toast.makeText(mContext, "Error: Field can't be blank", Toast.LENGTH_SHORT).show();
                                    Log.i("exitt","eee");
                                    return;

                                }
                                Log.i("finall add",adder);
                                dbrefer.child(adder).setValue(adder);
                                Toast.makeText(mContext,"New Element added successfully,Swipe Down to Refresh",Toast.LENGTH_LONG).show();
//                                listFragment.reload();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });
                        alert.show();
                    }
                    else {
                        Toast.makeText(mContext,"To Add or Delete an item , Please click on the buttons above",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }

            });

            //set click listener on LinearLayout because the click area is bigger than the ImageView
            mExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set click event on expand button here
                    mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                    // rotate the icon based on the current state
                    // but only here because otherwise we'd see the animation on expanded items too while scrolling
                    mExpandIcon.animate().rotation(mListItems.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();

                    //Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", getAdapterPosition(), mItem.isExpanded()), Toast.LENGTH_SHORT).show();
                }
            });


        }
        public Integer parentgive(int level,int x){

            int parentlevel = mListItems.get(getAdapterPosition() - x).getLevel();
            while (parentlevel >= level) {

                if (level == 0) {
                    break;
                }
                x++;
                parentlevel = mListItems.get(getAdapterPosition() - x).getLevel();

            }
            return x;
        }
    }
}
