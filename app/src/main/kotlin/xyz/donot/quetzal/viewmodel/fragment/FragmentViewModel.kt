package xyz.donot.quetzal.viewmodel.fragment

import xyz.donot.quetzal.util.getTwitterInstance

abstract class FragmentViewModel {
    val twitter by lazy { getTwitterInstance() }

}