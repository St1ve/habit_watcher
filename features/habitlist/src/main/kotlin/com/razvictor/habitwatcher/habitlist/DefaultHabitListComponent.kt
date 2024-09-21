package com.razvictor.habitwatcher.habitlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultHabitListComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
) : HabitListComponent, ComponentContext by componentContext {

        private val _uiState = MutableValue(HabitListUiState(emptyList()))
//    private val _uiState = MutableValue<HabitListUiState>(
//        // FIXME: Replace by empty list
//        HabitListUiState(
//            listOf(
//                HabitListUiState.HabitUiState(
//                    id = 1,
//                    name = "Habit very long text dalfkj;lasdjkf ;laksjdf;l kajs;ldfkja;lsdkjf ;aksjd;lkajsd;lkf 1",
//                    isDone = true,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 2,
//                    name = "Habit 2",
//                    isDone = false,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 3,
//                    name = "Habit 3",
//                    isDone = true,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 4,
//                    name = "Habit 4",
//                    isDone = false,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 5,
//                    name = "Habit 4",
//                    isDone = false,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 6,
//                    name = "Habit 4",
//                    isDone = false,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 7,
//                    name = "Habit 4",
//                    isDone = false,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 8,
//                    name = "Habit 4",
//                    isDone = false,
//                ),
//                HabitListUiState.HabitUiState(
//                    id = 9,
//                    name = "Habit 4",
//                    isDone = false,
//                ),
//            )
//        )
//    )
    override val uiState: Value<HabitListUiState> = _uiState

    @AssistedFactory
    interface Factory : HabitListComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultHabitListComponent
    }
}
