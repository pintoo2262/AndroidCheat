
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by smn on 17/2/17.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int mNumCol;

    public SpacesItemDecoration(int space, int numCol) {
        this.space = space;
        this.mNumCol=numCol;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;
        int position=parent.getChildLayoutPosition(view);

        if(mNumCol<=2) {
            if (position == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            } else {
                if ((position % mNumCol) != 0) {
                    outRect.left = space / 2;
                    outRect.right = space;
                } else {
                    outRect.left = space;
                    outRect.right = space / 2;
                }
            }
        }else{
            if (position == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            } else {
                if ((position % mNumCol) == 0) {
                    outRect.left = space;
                    outRect.right = space/2;
                } else if((position % mNumCol) == (mNumCol-1)){
                    outRect.left = space/2;
                    outRect.right = space;
                }else{
                    outRect.left=space/2;
                    outRect.right=space/2;
                }
            }

        }

        if(position<mNumCol){
            outRect.top=space;
        }else{
            outRect.top=0;
        }
    }
}
