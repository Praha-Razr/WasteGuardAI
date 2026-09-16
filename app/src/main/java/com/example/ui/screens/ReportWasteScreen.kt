package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.WasteGuardMapView
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportWasteScreen(
    currentLanguage: AppLanguage,
    selectedSampleTag: String?,
    capturedPhotoBitmap: Bitmap? = null,
    onSelectSamplePhoto: (String, Bitmap?) -> Unit,
    onNavigate: (Screen) -> Unit,
    onRunAiAnalysis: (Double, Double, String) -> Unit
) {
    val context = LocalContext.current

    var description by remember { mutableStateOf("") }
    var locationAddress by remember { mutableStateOf("M.G. Road, Near Stormwater Drain, Ward 18, Chennai") }
    var latitude by remember { mutableDoubleStateOf(13.0827) }
    var longitude by remember { mutableDoubleStateOf(80.2707) }
    var gpsStatusText by remember { mutableStateOf("GPS Position Ready") }
    var isFetchingGps by remember { mutableStateOf(false) }

    // Runtime Permission States
    var cameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Function to acquire live device GPS position
    fun fetchLiveLocation() {
        if (!locationPermissionGranted) {
            gpsStatusText = "Permission Required for Live GPS"
            return
        }

        isFetchingGps = true
        gpsStatusText = "Acquiring GPS lock..."

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    isFetchingGps = false
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        gpsStatusText = "Live GPS Locked (Accuracy: ±${location.accuracy.toInt()}m)"
                        locationAddress = "Live Location: ${String.format("%.4f", latitude)}° N, ${String.format("%.4f", longitude)}° E (Ward 18, Chennai)"
                    } else {
                        // Fallback to LocationManager if FusedLocation returns null
                        try {
                            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            val gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (gpsLoc != null) {
                                latitude = gpsLoc.latitude
                                longitude = gpsLoc.longitude
                                gpsStatusText = "Live GPS Locked via Provider"
                                locationAddress = "Ward 18, Chennai (${String.format("%.4f", latitude)}° N, ${String.format("%.4f", longitude)}° E)"
                            } else {
                                gpsStatusText = "GPS Active (Using Ward 18 Coordinates)"
                            }
                        } catch (e: Exception) {
                            gpsStatusText = "GPS Position Ready"
                        }
                    }
                }
                .addOnFailureListener {
                    isFetchingGps = false
                    gpsStatusText = "GPS Active (Ward 18 Location)"
                }
        } catch (e: SecurityException) {
            isFetchingGps = false
            gpsStatusText = "Location Permission Required"
        } catch (e: Exception) {
            isFetchingGps = false
            gpsStatusText = "GPS Position Ready"
        }
    }

    // Multiple Permission Request Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: cameraPermissionGranted
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        locationPermissionGranted = fineGranted || coarseGranted || locationPermissionGranted

        if (locationPermissionGranted) {
            fetchLiveLocation()
        }
    }

    // Camera Capture Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            onSelectSamplePhoto("live_captured", bitmap)
        }
    }

    // Gallery Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    onSelectSamplePhoto("live_captured", bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Request permissions on screen launch if missing
    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted || !locationPermissionGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            fetchLiveLocation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Waste", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Screen.CITIZEN_HOME) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenDark,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Permission Request Banner (if any permission is missing)
            if (!cameraPermissionGranted || !locationPermissionGranted) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCD34D)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Permissions Needed",
                                tint = RiskMedium
                            )
                            Text(
                                text = "Device Permissions Requested",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = SlateNavy
                            )
                        }

                        Text(
                            text = "WasteGuard AI requires Camera access to photograph waste piles and GPS access to tag accurate location for cleanup teams.",
                            fontSize = 12.sp,
                            color = SlateNavyLight
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (cameraPermissionGranted) "📷 Camera: Granted ✓" else "📷 Camera: Pending ❌",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (cameraPermissionGranted) GreenPrimary else RiskHigh
                            )
                            Text(
                                text = if (locationPermissionGranted) "📍 GPS: Granted ✓" else "📍 GPS: Pending ❌",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (locationPermissionGranted) GreenPrimary else RiskHigh
                            )
                        }

                        Button(
                            onClick = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Grant Camera & GPS Permissions", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Live Camera Photo Capture & Sample Scenarios Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "📸 " + StringsDictionary.get("see_problem", currentLanguage),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateNavy
                    )

                    // Display Live Photo Thumbnail if available
                    if (capturedPhotoBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, GreenPrimary, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = capturedPhotoBitmap.asImageBitmap(),
                                contentDescription = "Captured Photo",
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GreenDark.copy(alpha = 0.85f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "📸 Photo Captured Ready for AI ✓",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Live Camera & Gallery Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (cameraPermissionGranted) {
                                    cameraLauncher.launch(null)
                                } else {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Take Photo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                galleryLauncher.launch("image/*")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Collections, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("From Gallery", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GreenPrimary)
                        }
                    }

                    Divider(color = GreenBorder)

                    // Photo Preset Choice Scenarios (Hackathon Demo Scenarios)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Or Select Demo Photo Scenario:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenDark
                        )

                        // Option 1: Demo Plastic Pile Scenario 13
                        val isPlasticSelected = (selectedSampleTag == "plastic_pile_drain" || selectedSampleTag == "demo_scenario_13") && capturedPhotoBitmap == null
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isPlasticSelected) Color(0xFFECFDF5) else Color(0xFFF8FAF9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.5.dp,
                                    if (isPlasticSelected) GreenPrimary else GreenBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectSamplePhoto("plastic_pile_drain", null) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("🚨", fontSize = 20.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Plastic Waste Pile near Stormwater Drain",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = SlateNavy
                                    )
                                    Text(
                                        text = "Ward 18 Chennai • Stormwater block risk",
                                        fontSize = 11.sp,
                                        color = SlateNavyLight
                                    )
                                }
                                if (isPlasticSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary)
                                }
                            }
                        }

                        // Option 2: Organic Market Waste
                        val isOrganicSelected = selectedSampleTag == "organic_waste" && capturedPhotoBitmap == null
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isOrganicSelected) Color(0xFFECFDF5) else Color(0xFFF8FAF9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.5.dp,
                                    if (isOrganicSelected) GreenPrimary else GreenBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectSamplePhoto("organic_waste", null) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("🥬", fontSize = 20.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Organic Market Packaging Dump",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = SlateNavy
                                    )
                                    Text(
                                        text = "Ward 45 Chennai • Routine market waste",
                                        fontSize = 11.sp,
                                        color = SlateNavyLight
                                    )
                                }
                                if (isOrganicSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary)
                                }
                            }
                        }

                        // Option 3: Electronic Scrap
                        val isEwasteSelected = selectedSampleTag == "e_waste" && capturedPhotoBitmap == null
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isEwasteSelected) Color(0xFFECFDF5) else Color(0xFFF8FAF9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.5.dp,
                                    if (isEwasteSelected) GreenPrimary else GreenBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectSamplePhoto("e_waste", null) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("🔋", fontSize = 20.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "E-Waste & Batteries Dump",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = SlateNavy
                                    )
                                    Text(
                                        text = "Ward 5 Madurai • Battery hazard alert",
                                        fontSize = 11.sp,
                                        color = SlateNavyLight
                                    )
                                }
                                if (isEwasteSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // GPS Location Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, tint = GreenPrimary)
                            Text(
                                text = "📍 " + StringsDictionary.get("location_detected", currentLanguage),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = SlateNavy
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GreenLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder)
                        ) {
                            Text(
                                text = if (locationPermissionGranted) "GPS Granted ✓" else "GPS Pending",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (locationPermissionGranted) GreenPrimary else RiskHigh,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Coordinates: ${String.format("%.4f", latitude)}° N, ${String.format("%.4f", longitude)}° E",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SlateNavy
                            )
                            Text(
                                text = gpsStatusText,
                                fontSize = 11.sp,
                                color = SlateNavyLight
                            )
                        }

                        IconButton(
                            onClick = {
                                if (locationPermissionGranted) {
                                    fetchLiveLocation()
                                } else {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = "Refresh GPS Location",
                                tint = GreenPrimary
                            )
                        }
                    }

                    OutlinedTextField(
                        value = locationAddress,
                        onValueChange = { locationAddress = it },
                        label = { Text("Approximate Address & Ward") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    WasteGuardMapView(
                        incidents = emptyList(),
                        hotspots = emptyList(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }
            }

            // Description Input
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = StringsDictionary.get("tell_us_more", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SlateNavy
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("e.g. Plastic waste pile is clogging the stormwater drain intake grate.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Action Button: ANALYZE & REPORT
            Button(
                onClick = { onRunAiAnalysis(latitude, longitude, description) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Text(
                        text = StringsDictionary.get("analyze_and_report", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
