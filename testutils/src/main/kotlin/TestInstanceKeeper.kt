import com.arkivanov.essenty.instancekeeper.InstanceKeeper

class TestInstanceKeeper(
    private val mockInstance: InstanceKeeper.Instance
) : InstanceKeeper {
    override fun get(key: Any): InstanceKeeper.Instance {
        return mockInstance
    }

    override fun put(key: Any, instance: InstanceKeeper.Instance) {
    }

    override fun remove(key: Any): InstanceKeeper.Instance {
        return mockInstance
    }
}
