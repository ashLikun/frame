package utils.ashlikun.com.utils.anim;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ivy on 2017/3/21.
 * Description：
 */

public class SlideItemAnimator extends BaseItemAnimator {

    @Override
    public void setRemoveAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.translationX(-holder.itemView.getWidth());
    }

    @Override
    public void removeAnimationEnd(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationX(0);
    }

    @Override
    public void addAnimationInit(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationX(-holder.itemView.getWidth());
    }

    @Override
    public void setAddAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.translationX(0);
    }

    @Override
    public void addAnimationCancel(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, 0);
    }

    @Override
    public void setOldChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.translationX(-holder.itemView.getWidth());
    }

    @Override
    public void oldChangeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, 0);
    }

    @Override
    public void newChangeAnimationInit(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, holder.itemView.getWidth());
    }

    @Override
    public void setNewChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.translationX(0);
    }

    @Override
    public void newChangeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, 0);
    }
}
