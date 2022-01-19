package com.umang.thegymberapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.SimpleItemAnimator
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.umang.thegymberapp.R
import com.umang.thegymberapp.databinding.FragmentGymsBinding
import com.umang.thegymberapp.ui.viewmodel.GymsViewModel
import com.umang.thegymberapp.ui.adapters.GymsAdapter
import com.umang.thegymberapp.utils.DENIED
import com.umang.thegymberapp.utils.GRANTED
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * To display gyms as cards with swipe left/right feature to like dislike gyms
 *
 */
@AndroidEntryPoint
class GymsFragment : Fragment(), CardStackListener {

    private var randomNum: Int = 0 // used to decide match from number of swipe done by user
    private var cardSwipeCounter: Int = 0 // increase when right swipe done, reset after 10 swipes
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>> // used to manage location permission
    private lateinit var adapter: GymsAdapter
    private lateinit var manager: CardStackLayoutManager // gyms card view manager
    private lateinit var fragmentGymsBinding: FragmentGymsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient // fused client to fetch location

    private val viewModel: GymsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentGymsBinding = FragmentGymsBinding.inflate(inflater, container, false)
        fragmentGymsBinding.lifecycleOwner = viewLifecycleOwner
        fragmentGymsBinding.viewModel = viewModel
        return fragmentGymsBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        randomNum = (0..9).random() // generate first random num
        registerForLocationResults()
        setupAdapter()
        setupDataObservers()
        setClickListeners()
        viewModel.getGyms(false)
    }

    /**
     * init click listeners for like/dislike/matched gyms
     *
     */
    private fun setClickListeners() {
        //to like gym without swipe
        fragmentGymsBinding.ivLike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            fragmentGymsBinding.cardStackView.swipe()
        }

        //to dislike gym without swipe
        fragmentGymsBinding.ivDisLike.setOnClickListener {

            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            fragmentGymsBinding.cardStackView.swipe()

        }
        //to open matched gym list
        fragmentGymsBinding.ivMatchedGyms.setOnClickListener {
            openGymMatchesFragment()
        }
    }

    /**
     * To check location permission result
     *
     */
    private fun registerForLocationResults() {
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLastKnownLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLastKnownLocation()
                }
                else -> {
                    viewModel.onRequestPermissionResult(DENIED)
                }
            }
        }

    }

    /**
     * Ask for location permission after data fetched from repository
     *
     */
    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )


    }

    /**
     * To fetch current location
     *
     */
    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    viewModel.onRequestPermissionResult(GRANTED, location)
                }
        }

    }

    /**
     * To handle one shot events
     *
     */
    private fun setupDataObservers() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    //to show common error as toast
                    viewModel.commonError.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                launch {
                    viewModel.requestLocationPermission.collect {
                        requestLocationPermission()
                    }
                }
            }

        }
    }

    /**
     * Setup swipe cards adapter
     *
     */
    private fun setupAdapter() {
        (fragmentGymsBinding.cardStackView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        adapter = GymsAdapter()
        manager = CardStackLayoutManager(requireContext(), this)
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(90.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        fragmentGymsBinding.cardStackView.layoutManager = manager
        fragmentGymsBinding.cardStackView.adapter = adapter
        fragmentGymsBinding.cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    /**
     * To open matched gyms fragment
     *
     */
    private fun openGymMatchesFragment() {
        val gymMatchesListFragment = GymMatchesListFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_left,
                R.anim.wait_anim,
                R.anim.wait_anim,
                R.anim.slide_right
            )
            .addToBackStack(null)
            .add(R.id.fragment_container_view, gymMatchesListFragment)
            .commit()

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    /**
     * To get matched gym by using [randomNum], from each 10 swipes it will pick one gym to match
     *
     * @param direction : swipe direction
     */
    override fun onCardSwiped(direction: Direction?) {
        if(direction == Direction.Right) {
            if (cardSwipeCounter >= 10) {
                cardSwipeCounter = 0
                randomNum = (0..9).random()
            }
            if (cardSwipeCounter == randomNum) {
                animateMatch()
                viewModel.matchOrRejectGym(manager.topPosition - 1, direction)
            }
            cardSwipeCounter++
        }else{
            viewModel.matchOrRejectGym(manager.topPosition - 1, direction)
        }

        if(manager.topPosition>=adapter.itemCount){
            viewModel.getGyms(true)
        }


    }

    /**
     * To show gym matched animation
     *
     */
    private fun animateMatch() {
        manager.setSwipeableMethod(SwipeableMethod.None)
        fragmentGymsBinding.animateView.visibility = View.VISIBLE
        val containerMiddleX: Int = fragmentGymsBinding.container.width / 2
        val containerMiddleY: Int = fragmentGymsBinding.container.height / 2
        val ivMatchLocation = fragmentGymsBinding.ivMatchedGyms.y
        fragmentGymsBinding.ivMatchedGyms.animate().setDuration(1000).scaleXBy(2.0f).scaleYBy(2.0f)
        fragmentGymsBinding.ivMatchedGyms.animate().setDuration(1000).y(containerMiddleY.toFloat())
        fragmentGymsBinding.animateView.animate().setDuration(1000).alpha(1.0f).interpolator =
            AccelerateInterpolator()
        Handler(Looper.getMainLooper()).postDelayed({
            CommonConfetti.explosion(
                fragmentGymsBinding.container,
                containerMiddleX,
                containerMiddleY,
                intArrayOf(ContextCompat.getColor(requireContext(),R.color.animation_overlay))
            )
                .stream(1000)
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            fragmentGymsBinding.ivMatchedGyms.animate().setDuration(1000).scaleX(1.0f).scaleY(1.0f)
            fragmentGymsBinding.ivMatchedGyms.animate().setDuration(1000).y(ivMatchLocation).interpolator =
                AccelerateInterpolator()
            fragmentGymsBinding.animateView.animate().alpha(0.0f).duration = 1000
        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            fragmentGymsBinding.animateView.visibility = View.GONE
            manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        }, 4000)

    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }


}