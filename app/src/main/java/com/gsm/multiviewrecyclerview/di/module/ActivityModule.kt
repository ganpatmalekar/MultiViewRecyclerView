package com.gsm.multiviewrecyclerview.di.module

import com.gsm.multiviewrecyclerview.ui.adapter.MainUiAdapter
import com.gsm.multiviewrecyclerview.ui.adapter.UserReviewAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    fun providesMainUiAdapter(): MainUiAdapter = MainUiAdapter()

    @Provides
    fun providesReviewAdapter(): UserReviewAdapter = UserReviewAdapter()
}