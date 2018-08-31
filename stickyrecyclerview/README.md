# **stickyrecyclerview**
recycleview粘性头部

      headerAdapter = new CommonHeaderAdapter<LoopViewData>
                (this, R.layout.head_item_list, listDatas) {
            @Override
            public boolean isHeader(int position) {
                return position == 3 || position == 8 || position == 10 || position == 20 || position == 15;
            }

            @Override
            public void convert(StickyViewHolder holder, LoopViewData loopViewData) {
                TextView tv = holder.getView(R.id.textView);
                tv.setText(loopViewData.getShowText());
            }

        };

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.addItemDecoration(new StickyHeadersBuilder()
                .setRecyclerView(recyclerView)
                .setSticky(true)
                .setOnHeaderClickListener(new OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, long headerId) {
                        ToastUtils.showLong(getApplication(), headerId + "");
                    }
                })
                .setAdapter(adapter)
                .setStickyHeadersAdapter(headerAdapter, false)
                .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



