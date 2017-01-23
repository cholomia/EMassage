package com.capstone.tip.emassage.ui.text_twist;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemLetterBinding;
import com.capstone.tip.emassage.model.pojo.Letter;
import com.capstone.tip.emassage.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author pocholomia
 * @since 23/01/2017
 */

public class LetterListAdapter extends RecyclerView.Adapter<LetterListAdapter.ViewHolder> {

    private final List<Letter> letters;
    private TextTwistView view;
    private boolean choice;

    public LetterListAdapter(TextTwistView view, boolean choice) {
        this.view = view;
        this.choice = choice;
        letters = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLetterBinding itemLetterBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_letter, parent, false);
        return new ViewHolder(itemLetterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemLetterBinding.setLetter(letters.get(position));
        holder.itemLetterBinding.setPosition(position);
        holder.itemLetterBinding.setChoice(choice);
        holder.itemLetterBinding.setView(view);
        holder.itemLetterBinding.cardLetter.setRadius(SizeUtils.dpToPx(choice ? 24 : 4));
    }

    @Override
    public int getItemCount() {
        return letters.size();
    }

    void setLetters(List<Letter> letters) {
        this.letters.clear();
        this.letters.addAll(letters);
        notifyDataSetChanged();
    }

    void clear() {
        this.letters.clear();
        notifyDataSetChanged();
    }

    void shuffle() {
        Collections.shuffle(letters);
        notifyDataSetChanged();
    }

    String getAnswer() {
        String answer = "";
        for (Letter letter : letters) answer += letter.getLetter();
        return answer;
    }

    int getEmptyIndex() {
        for (int i = 0; i < letters.size(); i++)
            if (letters.get(i).isEmpty()) return i;
        return -1;
    }

    void removeLetter(int position) {
        Letter letter = letters.get(position);
        letter.setLetter("");
        letters.set(position, letter);
        notifyItemChanged(position);
    }

    void addLetter(String strLetter, int emptyIndex) {
        Letter letter = letters.get(emptyIndex);
        letter.setLetter(strLetter);
        letters.set(emptyIndex, letter);
        letters.get(emptyIndex).getLetter();
        notifyItemChanged(emptyIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLetterBinding itemLetterBinding;

        public ViewHolder(ItemLetterBinding itemLetterBinding) {
            super(itemLetterBinding.getRoot());
            this.itemLetterBinding = itemLetterBinding;
        }
    }
}
