package iammert.com.expandablelib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mertsimsek on 28/07/2017.
 */

public class ExpandableLayout extends LinearLayout {

    public interface Renderer<P, C> {
        void renderParent(View view, P model, boolean isExpanded, int parentPosition);

        void renderChild(View view, C model, int parentPosition, int childPosition);
    }

    private static final int NO_RES = 0;

    private static final int NO_INDEX = -1;

    private LayoutInflater layoutInflater;

    @LayoutRes
    private int parentLayout;

    @LayoutRes
    private int childLayout;

    private Renderer renderer;

    private List<Section> sections;

    private ExpandCollapseListener.ExpandListener expandListener;

    private ExpandCollapseListener.CollapseListener collapseListener;

    private boolean onlyOneSectionExpanded = false;

    public ExpandableLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandableLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setOrientation(VERTICAL);
        sections = new ArrayList<>();
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableLayout);

        parentLayout = typedArray.getResourceId(R.styleable.ExpandableLayout_parentLayout, NO_RES);
        childLayout = typedArray.getResourceId(R.styleable.ExpandableLayout_childLayout, NO_RES);
        layoutInflater = LayoutInflater.from(context);

        typedArray.recycle();
    }

    public <P> void setExpandListener(ExpandCollapseListener.ExpandListener<P> expandListener) {
        this.expandListener = expandListener;
    }

    public <P> void setCollapseListener(ExpandCollapseListener.CollapseListener<P> collapseListener) {
        this.collapseListener = collapseListener;
    }

    public void setRenderer(@NonNull Renderer renderer) {
        this.renderer = renderer;
    }

    public void addSection(@NonNull Section section) {
        sections.add(section);
        notifySectionAdded(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public <P, C> void addChild(P parent, C child) {
        int parentIndex = NO_INDEX;
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).parent.equals(parent)) {
                if (!sections.get(i).children.contains(child)) {
                    sections.get(i).children.add(child);
                }
                parentIndex = i;
            }
        }
        if (parentIndex != NO_INDEX) {
            notifyItemAdded(parentIndex, child);
            if (sections.get(parentIndex).expanded) {
                expand(parent);
            }
        }
    }

    public <P, C> void addChildren(P parent, List<C> children) {
        int parentIndex = NO_INDEX;
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).parent.equals(parent)) {
                if (!sections.get(i).children.containsAll(children)) {
                    sections.get(i).children.addAll(children);
                }
                parentIndex = i;
            }
        }
        if (parentIndex != NO_INDEX) {
            notifyItemAdded(parentIndex, children);
            if (sections.get(parentIndex).expanded) {
                expand(parent);
            }
        }
    }

    private <C> void notifyItemAdded(int parentIndex, C child) {
        if (renderer == null) {
            return;
        }
        ViewGroup parentView = (ViewGroup) getChildAt(parentIndex);
        View childView = layoutInflater.inflate(childLayout, null);
        renderer.renderChild(childView, child, parentIndex, sections.get(parentIndex).children.size() - 1);
        parentView.addView(childView);
    }

    private <C> void notifyItemAdded(int parentIndex, List<C> children) {
        if (renderer == null) {
            return;
        }
        ViewGroup parentView = (ViewGroup) getChildAt(parentIndex);
        for (int i = 0; i < children.size(); i++) {
            View childView = layoutInflater.inflate(childLayout, null);
            renderer.renderChild(childView, children.get(i), parentIndex, i);
            parentView.addView(childView);
        }
    }

    private void notifySectionAdded(final Section section) {
        if (renderer == null)
            return;

        LinearLayout sectionLayout = new LinearLayout(getContext());
        sectionLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        sectionLayout.setOrientation(LinearLayout.VERTICAL);

        View parentView = layoutInflater.inflate(parentLayout, null);
        parentView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (section.expanded)
                {
                    collapse(section.parent);
                }
                else
                {
                    if (onlyOneSectionExpanded)
                    {
                        for (Section sec : sections)
                        {
                            if (!sec.equals(section))
                            {
                                collapse(sec.parent);
                            }
                        }
                    }
                    expand(section.parent);
                }
            }
        });
        renderer.renderParent(parentView, section.parent, section.expanded, sections.size() - 1);
        sectionLayout.addView(parentView);

        if (section.expanded) {
            for (int i = 0; i < section.children.size(); i++) {
                Object child = section.children.get(i);
                View childView = layoutInflater.inflate(childLayout, null);
                renderer.renderChild(childView, child, sections.size() - 1, i);
                sectionLayout.addView(childView);
            }
        }
        addView(sectionLayout);
    }

    public void notifyParentChanged(int position) {
        if (position > getChildCount() - 1) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(position);
        if (viewGroup != null && viewGroup.getChildCount() > 0) {
            View parentView = viewGroup.getChildAt(0);
            renderer.renderParent(parentView, sections.get(position).parent, sections.get(position).expanded, position);
        }
    }

    private <P> void expand(@NonNull P parent) {
        for (int i = 0; i < sections.size(); i++) {
            if (parent.equals(sections.get(i).parent)) {
                ViewGroup sectionView = ((ViewGroup) getChildAt(i));
                sectionView.removeViews(1, sectionView.getChildCount() - 1);
                sections.get(i).expanded = true;
                notifyItemAdded(i, sections.get(i).children);
                if (expandListener != null)
                    expandListener.onExpanded(i, sections.get(i).parent, sectionView.getChildAt(0));
                break;
            }
        }
    }

    private <P> void collapse(@NonNull P parent) {
        for (int i = 0; i < sections.size(); i++) {
            if (parent.equals(sections.get(i).parent)) {
                ViewGroup sectionView = ((ViewGroup) getChildAt(i));
                sections.get(i).expanded = false;
                sectionView.removeViews(1, sectionView.getChildCount() - 1);
                if (collapseListener != null)
                    collapseListener.onCollapsed(i, sections.get(i).parent, sectionView.getChildAt(0));
                break;
            }
        }
    }

    public void collapseSection(Section section)
    {
        collapse(section.parent);
    }

    public void collapseAllSection()
    {
        for (Section section : sections)
        {
            collapseSection(section);
        }
    }

    public void expandSection(Section section)
    {
        expand(section.parent);
    }

    public void expandAllSection()
    {
        for (Section section : sections)
        {
            expandSection(section);
        }
    }

    public void setOnlyOneSectionExpanded(boolean onlyOneSectionExpanded)
    {
        this.onlyOneSectionExpanded = onlyOneSectionExpanded;
    }

    public boolean isOnlyOneSectionExpanded()
    {
        return onlyOneSectionExpanded;
    }
}
