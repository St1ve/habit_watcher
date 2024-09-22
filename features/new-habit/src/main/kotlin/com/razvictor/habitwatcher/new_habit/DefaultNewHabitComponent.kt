package com.razvictor.habitwatcher.new_habit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.razvictor.habitwatcher.db.dao.HabitDao
import com.razvictor.habitwatcher.db.entities.HabitEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DefaultNewHabitComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    private val habitDao: HabitDao,
) : NewHabitComponent, ComponentContext by componentContext {

    private val newHabitRetainedInstance = instanceKeeper.getOrCreate { NewHabitRetainedInstance(habitDao) }

    override fun onCreateHabitClick() {
        newHabitRetainedInstance.createNewHabit()
    }

    @AssistedFactory
    interface Factory : NewHabitComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultNewHabitComponent
    }
}

// FIXME: Make it like repository
internal class NewHabitRetainedInstance(
//    mainContext: CoroutineContext,
    private val habitDao: HabitDao,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob())

    fun createNewHabit() {
        scope.launch {
            habitDao.insertHabits(HabitEntity(name = "Test"))
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
