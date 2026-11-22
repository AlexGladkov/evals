package com.dev.nicehash.helpers;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dev.nicehash.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by agladkov on 22.12.17.+
 * Import library for RecyclerView Config
 * @link https://github.com/drstranges/DataBinding_For_RecyclerView
 */
public class ListConfig {

    private RecyclerView.Adapter<?> mAdapter;
    private LayoutManagerProvider mLayoutManagerProvider;
    private RecyclerView.ItemAnimator mItemAnimator;
    private List<RecyclerView.ItemDecoration> mItemDecorations;
    private List<RecyclerView.OnScrollListener> mScrollListeners;
    private ItemTouchHelper mItemTouchHelper;
    private boolean mHasFixedSize;
    private boolean mHasNestedScroll;

    public ListConfig(RecyclerView.Adapter<?> adapter, LayoutManagerProvider layoutManagerProvider,
                      RecyclerView.ItemAnimator itemAnimator,
                      List<RecyclerView.ItemDecoration> itemDecorations,
                      List<RecyclerView.OnScrollListener> scrollListeners,
                      ItemTouchHelper itemTouchHelper, boolean hasFixedSize, boolean hasNestedScroll) {
        this.mAdapter = adapter;
        this.mLayoutManagerProvider = layoutManagerProvider;
        this.mItemAnimator = itemAnimator;
        this.mItemDecorations = itemDecorations != null ? itemDecorations : Collections.emptyList();
        this.mScrollListeners = scrollListeners != null ? scrollListeners : Collections.emptyList();
        this.mItemTouchHelper = itemTouchHelper;
        this.mHasFixedSize = hasFixedSize;
        this.mHasNestedScroll = hasNestedScroll;
    }

    /**
     * Applies defined configuration for RecyclerView
     *
     * @param context      the context
     * @param recyclerView the target recycler view for applying the configuration
     */
    public void applyConfig(Context context, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = mLayoutManagerProvider != null
                ? mLayoutManagerProvider.get(context) : null;
        if (mAdapter == null || mLayoutManagerProvider == null || layoutManager == null) return;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(mHasFixedSize);
        recyclerView.setNestedScrollingEnabled(mHasNestedScroll);
        recyclerView.setAdapter(mAdapter);

        for (RecyclerView.ItemDecoration decoration : mItemDecorations) {
            recyclerView.addItemDecoration(decoration);
        }

        if (mScrollListeners != null) {
            for (RecyclerView.OnScrollListener listener : mScrollListeners) {
                recyclerView.addOnScrollListener(listener);
            }
        }

        if (mItemAnimator != null) {
            recyclerView.setItemAnimator(mItemAnimator);
        }

        if (mItemTouchHelper != null) {
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    /**
     * Builder for setting ListConfig
     * Sample:
     * <pre>
     * {@code
     *      ListConfig listConfig = new ListConfig.Builder(mAdapter)
     *          .setLayoutManagerProvider(new SimpleGridLayoutManagerProvider(mSpanCount, getSpanSizeLookup()))
     *          .addItemDecoration(new ColorDividerItemDecoration(color, spacing, SPACE_LEFT|SPACE_TOP, false))
     *          .setDefaultDividerEnabled(true)
     *          .addOnScrollListener(new OnLoadMoreScrollListener(mCallback))
     *          .setItemAnimator(getItemAnimator())
     *          .setHasFixedSize(true)
     *          .setItemTouchHelper(getItemTouchHelper())
     *          .build(context);
     * }
     * </pre>
     * If LinearLayoutManager will be used by default
     */
    public static class Builder {
        private final String TAG = Builder.class.getSimpleName();
        private RecyclerView.Adapter<?> mAdapter;
        private LayoutManagerProvider mLayoutManagerProvider = null;
        private RecyclerView.ItemAnimator mItemAnimator = null;
        private ArrayList<RecyclerView.ItemDecoration> mItemDecorations = null;
        private ArrayList<RecyclerView.OnScrollListener> mOnScrollListeners = null;
        private ItemTouchHelper mItemTouchHelper = null;
        private boolean mHasNestedScroll = false;
        private boolean mHasFixedSize = false;
        private int mDefaultDividerSize = -1;

        public Builder(RecyclerView.Adapter<?> adapter) {
            this.mAdapter = adapter;
        }

        /**
         * Set Layout manager provider. If not set default LinearLayoutManager will be applied
         *
         * @param layoutManagerProvider the layout manager provider. Can be custom or one of
         * simple: SimpleLinearLayoutManagerProvider,
         * SimpleGridLayoutManagerProvider or
         * SimpleStaggeredGridLayoutManagerProvider.
         * @return the builder
         */
        public Builder setLayoutManagerProvider(LayoutManagerProvider layoutManagerProvider) {
            Log.e(TAG, "layout manager is set " + layoutManagerProvider);
            mLayoutManagerProvider = layoutManagerProvider;
            return this;
        }

        /**
         * Set RecyclerView.ItemAnimator
         *
         * @param itemAnimator the item animator
         * @return the builder
         */
        public Builder setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
            mItemAnimator = itemAnimator;
            return this;
        }

        /**
         * Set RecyclerView.ItemDecoration
         *
         * @return the builder
         */
        public Builder addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
            if (mItemDecorations == null) {
                mItemDecorations = new ArrayList<>();
            }

            mItemDecorations.add(itemDecoration);
            return this;
        }

        /**
         * Set RecyclerView.OnScrollListener
         *
         * @param onScrollListener the scroll listener.
         * @return the builder
         */
        public Builder addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
            if (mOnScrollListeners == null) {
                mOnScrollListeners = new ArrayList<>();
            }

            mOnScrollListeners.add(onScrollListener);
            return this;
        }

        /**
         * Set true if adapter changes cannot affect the size of the RecyclerView.
         * Applied to RecyclerView.setHasFixedSize
         *
         * @param isFixedSize true if RecyclerView items have fixed size
         * @return the builder
         */
        public Builder setHasFixedSize(boolean isFixedSize) {
            mHasFixedSize = isFixedSize;
            return this;
        }

        /**
         * Set true if adapter changes cannot affect the size of the RecyclerView.
         * Applied to RecyclerView.setNestedScrollingEnabled
         *
         * @param isNestedScroll true if RecyclerView items have nested scroll
         * @return the builder
         */
        public Builder setHasNestedScroll(boolean isNestedScroll) {
            mHasNestedScroll = isNestedScroll;
            return this;
        }

        /**
         * Set true to apply default divider with default size of 4dp.
         *
         * @param isEnabled set true to apply default divider.
         * @return the builder
         */
        public Builder setDefaultDividerEnabled(boolean isEnabled) {
            mDefaultDividerSize = isEnabled ? 0 : -1;
            return this;
        }

        /**
         * Enables defoult divider with custom size
         *
         * @param size
         * @return the builder
         */
        public Builder setDefaultDividerSize(int size) {
            mDefaultDividerSize = size;
            return this;
        }

        /**
         * Set ItemTouchHelper
         *
         * @param itemTouchHelper the ItemTouchHelper to apply for RecyclerView
         * @return the builder
         */
        public Builder setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
            mItemTouchHelper = itemTouchHelper;
            return this;
        }

        /**
         * Creates new ListConfig with defined configuration
         * If LayoutManagerProvider is not set, the SimpleLinearLayoutManagerProvider
         * will be used.
         *
         * @param context the context
         * @return the new ListConfig
         */
        public ListConfig build(Context context) {
            if (mLayoutManagerProvider == null)
                mLayoutManagerProvider = new SimpleLinearLayoutManagerProvider();

            if (mDefaultDividerSize >= 0) {
                if (mDefaultDividerSize == 0)
                    mDefaultDividerSize = context.getResources()
                            .getDimensionPixelSize(R.dimen.default_separator_size);
                /*if (mDefaultDividerSize == 0) mDefaultDividerSize = context.getResources()
                        .getDimensionPixelSize(R.dimen.rvdb_list_divider_size_default);
                addItemDecoration(new DividerItemDecoration(mDefaultDividerSize));*/
            }

            return new ListConfig(
                    mAdapter,
                    mLayoutManagerProvider,
                    mItemAnimator, mItemDecorations,
                    mOnScrollListeners,
                    mItemTouchHelper,
                    mHasFixedSize,
                    mHasNestedScroll);
        }
    }

    /**
     * The provider of LayoutManager for RecyclerView
     */
    public interface LayoutManagerProvider {
        RecyclerView.LayoutManager get(Context context);
    }

    /**
     * The simple LayoutManager provider for LinearLayoutManager
     */
    public static class SimpleLinearLayoutManagerProvider implements LayoutManagerProvider {
        @Override
        public RecyclerView.LayoutManager get(Context context) {
            return new LinearLayoutManager(context);
        }
    }

    public static class RefreshLinearLayoutManagerProvider implements LayoutManagerProvider {
        @Override
        public RecyclerView.LayoutManager get(Context context) {
            return new LinearLayoutManager(context);
        }
    }

    /**
     * The simple LayoutManager provider for LinearLayoutManager
     */
    public static class ReversedLinearLayoutManagerProvider implements LayoutManagerProvider {
        @Override
        public RecyclerView.LayoutManager get(Context context) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            return mLayoutManager;
        }
    }

    /**
     * The simple LayoutManager provider for GridLayoutManager
     */
    public static class SimpleGridLayoutManagerProvider implements LayoutManagerProvider {
        private final int mSpanCount;
        private GridLayoutManager.SpanSizeLookup mSpanSizeLookup = null;

        public SimpleGridLayoutManagerProvider(int mSpanCount) {
            this.mSpanCount = mSpanCount;
        }

        public SimpleGridLayoutManagerProvider(int spanCount, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
            mSpanCount = spanCount;
            mSpanSizeLookup = spanSizeLookup;
        }

        @Override
        public RecyclerView.LayoutManager get(Context context) {
            GridLayoutManager layoutManager = new GridLayoutManager(context, mSpanCount);
            if (mSpanSizeLookup != null) layoutManager.setSpanSizeLookup(mSpanSizeLookup);
            return layoutManager;
        }
    }

    /**
     * The simple LayoutManager provider for StaggeredGridLayoutManager
     */
    public static class SimpleStaggeredGridLayoutManagerProvider implements LayoutManagerProvider {
        private final int mSpanCount;
        private final int mOrientation;

        public SimpleStaggeredGridLayoutManagerProvider(int mSpanCount) {
            this(mSpanCount, StaggeredGridLayoutManager.VERTICAL);
        }

        public SimpleStaggeredGridLayoutManagerProvider(int mSpanCount, int mOrientation) {
            this.mSpanCount = mSpanCount;
            this.mOrientation = mOrientation;
        }

        @Override
        public RecyclerView.LayoutManager get(Context context) {
            return new StaggeredGridLayoutManager(mSpanCount, mOrientation);
        }
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            int itemCount = layoutManager != null ? layoutManager.getItemCount() : 0;

            // Add top margin only for the first item to avoid double space between
            if (itemCount == 4) {
                switch (parent.getChildLayoutPosition(view)) {
                    case 0: {
                        outRect.top = 0;
                        outRect.right = space;
                        outRect.left = 0;
                        outRect.bottom = space;
                        break;
                    }

                    case 1: {
                        outRect.top = 0;
                        outRect.right = 0;
                        outRect.left = space;
                        outRect.bottom = space;
                        break;
                    }

                    case 2: {
                        outRect.top = space;
                        outRect.right = space;
                        outRect.left = 0;
                        outRect.bottom = 0;
                        break;
                    }

                    case 3: {
                        outRect.top = space;
                        outRect.right = 0;
                        outRect.left = space;
                        outRect.bottom = 0;
                        break;
                    }
                }
            } else if (itemCount == 3) {
                switch (parent.getChildLayoutPosition(view)) {
                    case 0: {
                        outRect.top = 0;
                        outRect.right = 0;
                        outRect.left = 0;
                        outRect.bottom = 0;
                        break;
                    }

                    case 1: {
                        outRect.top = space * 2;
                        outRect.right = space;
                        outRect.left = 0;
                        outRect.bottom = 0;
                        break;
                    }

                    case 2: {
                        outRect.top = space * 2;
                        outRect.right = 0;
                        outRect.left = space;
                        outRect.bottom = 0;
                        break;
                    }
                }
            } else if (itemCount == 2) {
                switch (parent.getChildLayoutPosition(view)) {
                    case 0: {
                        outRect.top = 0;
                        outRect.right = space;
                        outRect.left = 0;
                        outRect.bottom = 0;
                        break;
                    }

                    case 1: {
                        outRect.top = 0;
                        outRect.right = 0;
                        outRect.left = space;
                        outRect.bottom = 0;
                        break;
                    }
                }
            } else if (itemCount == 3) {
                outRect.top = 0;
                outRect.right = 0;
                outRect.left = 0;
                outRect.bottom = 0;
            } else {
                int position = parent.getChildLayoutPosition(view);
                if (position > 1) {
                    if (position % 2 == 0) {
                        outRect.top = 0;
                        outRect.right = space;
                        outRect.left = 0;
                        outRect.bottom = space * 2;
                    } else {
                        outRect.top = 0;
                        outRect.right = 0;
                        outRect.left = space;
                        outRect.bottom = space * 2;
                    }
                } else {
                    outRect.top = 0;
                    outRect.right = position == 0 ? space : 0;
                    outRect.left = position == 0 ? 0 : space;
                    outRect.bottom = space * 2;
                }
            }
        }
    }
}
