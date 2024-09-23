package com.razvictor.habitwatcher.details

data class DetailsUiState(
    val headerState: HeaderState,
) {
    data class HeaderState(
        val title: String,
        val state: State,
    ) {
        enum class State {
            EDIT, DATA
        }

        companion object {
            val DEFAULT = HeaderState(
                title = "",
                state = State.DATA
            )
        }
    }

    companion object {
        val DEFAULT = DetailsUiState(
            headerState = HeaderState.DEFAULT,
        )
    }
}
