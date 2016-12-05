package com.capstone.tip.emassage.ui.quiz;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemChoiceBinding;
import com.capstone.tip.emassage.model.data.Choice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public class ChoiceListAdapter extends RecyclerView.Adapter<ChoiceListAdapter.ViewHolder> {

    private List<Choice> choices;
    private boolean[] selected;
    private boolean onBind;

    public ChoiceListAdapter() {
        choices = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemChoiceBinding itemChoiceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_choice,
                parent,
                false);
        return new ViewHolder(itemChoiceBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Choice choice = choices.get(position);
        holder.itemChoiceBinding.setChoice(choice);
        // issues when char increment is one liner
        char x = 'A';
        x += position;
        holder.itemChoiceBinding.setLetter(x + "");
        onBind = true;
        holder.itemChoiceBinding.checkbox.setChecked(selected[position]);
        onBind = false;
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    /**
     * choice list setup
     *
     * @param choiceList list to display
     */
    public void setChoiceList(List<Choice> choiceList) {
        this.choices.clear();
        this.choices.addAll(choiceList);
        resetSelected(-1);
    }

    /**
     * reset checkbox
     *
     * @param adapterPosition position to be checked
     */
    private void resetSelected(int adapterPosition) {
        selected = new boolean[choices.size()];
        for (int i = 0; i < choices.size(); i++) {
            selected[i] = i == adapterPosition;
        }
        if (!onBind)
            notifyDataSetChanged();
    }

    /**
     * @return selected choice
     */
    public Choice getSelectedChoice() {
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                return choices.get(i);
            }
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChoiceBinding itemChoiceBinding;

        public ViewHolder(ItemChoiceBinding itemChoiceBinding) {
            super(itemChoiceBinding.getRoot());
            this.itemChoiceBinding = itemChoiceBinding;
            // setup listener as issues if onBindViewHolder
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetSelected(getAdapterPosition());
                }
            });
            itemChoiceBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    resetSelected(getAdapterPosition());
                }
            });
        }
    }
}
