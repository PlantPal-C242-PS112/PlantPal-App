package com.android.plantpal.ui.plant

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.plantpal.ui.plant.analysis.AnalysisHistoryFragment
import com.android.plantpal.ui.plant.myplant.MyPlantsFragment
import com.android.plantpal.ui.plant.reminder.RemindersFragment

class PlantPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPlantsFragment()
            1 -> RemindersFragment()
            2 -> AnalysisHistoryFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
