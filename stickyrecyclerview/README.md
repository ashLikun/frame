# **stickyrecyclerview**
recycleview粘性头部
 headerAdapter = new CommonHeaderAdapter<LoopViewData, HeadItemListBinding>
                (this, R.layout.head_item_list, listDatas) {
            @Override
            public void convert(ViewHolder<HeadItemListBinding> holder, LoopViewData o) {
                holder.dataBind.setData(o);
                holder.dataBind.executePendingBindings();
            }

            @Override
            public long getHeaderId(int position) {
                return listDatas.get(position).getShowText().substring(0, 1).hashCode();
            }
        };

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
                recyclerView.addItemDecoration(new StickyHeadersBuilder()
                        .setRecyclerView(recyclerView.getRecyclerView())
                        .setAdapter(adapter)
                        .setStickyHeadersAdapter(headerAdapter, true)
                        .build());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setRefreshing(true);




