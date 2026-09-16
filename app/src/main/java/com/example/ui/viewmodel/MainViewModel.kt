package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiWasteAnalyzer
import com.example.ai.WasteAnalysisResult
import com.example.data.db.WasteGuardDatabase
import com.example.data.model.CleanupTeam
import com.example.data.model.Hotspot
import com.example.data.model.Incident
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.WasteGuardRepository
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    REGISTRATION,
    LOGIN,
    CITIZEN_HOME,
    CITIZEN_REPORT,
    CITIZEN_AI_ANALYSIS,
    CITIZEN_MY_REPORTS,
    CITIZEN_MAP,
    CITIZEN_PROFILE,
    AUTHORITY_DASHBOARD,
    AUTHORITY_HEATMAP,
    AUTHORITY_HOTSPOTS,
    AUTHORITY_INCIDENTS,
    AUTHORITY_INCIDENT_DETAIL,
    AUTHORITY_INSIGHTS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WasteGuardRepository

    private val _currentUser = MutableStateFlow(User())
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedIncidentId = MutableStateFlow<String?>("WG-1042")
    val selectedIncidentId: StateFlow<String?> = _selectedIncidentId.asStateFlow()

    private val _capturedPhotoBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedPhotoBitmap: StateFlow<Bitmap?> = _capturedPhotoBitmap.asStateFlow()

    private val _capturedSampleTag = MutableStateFlow<String?>("plastic_pile_drain")
    val capturedSampleTag: StateFlow<String?> = _capturedSampleTag.asStateFlow()

    private val _aiAnalysisResult = MutableStateFlow<WasteAnalysisResult?>(null)
    val aiAnalysisResult: StateFlow<WasteAnalysisResult?> = _aiAnalysisResult.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    val incidents: StateFlow<List<Incident>>
    val hotspots: StateFlow<List<Hotspot>>
    val teams: StateFlow<List<CleanupTeam>>

    init {
        val database = WasteGuardDatabase.getDatabase(application)
        repository = WasteGuardRepository(database)

        incidents = repository.allIncidents.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        hotspots = repository.allHotspots.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        teams = repository.allTeams.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.seedDemoDataIfEmpty()
        }
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ENGLISH) AppLanguage.TAMIL else AppLanguage.ENGLISH
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun loginAsCitizen() {
        _currentUser.value = User(
            id = "user_1",
            name = "Anand Kumar",
            role = UserRole.CITIZEN,
            district = "Chennai",
            municipality = "Greater Chennai Corporation",
            ward = "Ward 18"
        )
        _currentScreen.value = Screen.CITIZEN_HOME
    }

    fun loginAsAuthority() {
        _currentUser.value = User(
            id = "auth_1",
            name = "Er. S. Thirunavukkarasu",
            role = UserRole.MUNICIPAL_AUTHORITY,
            district = "Chennai",
            municipality = "Greater Chennai Corporation",
            ward = "Ward 18 - Sanitary Inspector"
        )
        _currentScreen.value = Screen.AUTHORITY_DASHBOARD
    }

    fun registerCitizen(name: String, email: String, phone: String, district: String, ward: String) {
        _currentUser.value = User(
            id = "user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            phone = phone,
            role = UserRole.CITIZEN,
            district = district,
            ward = ward
        )
        _currentScreen.value = Screen.CITIZEN_HOME
    }

    fun selectSamplePhoto(sampleTag: String, bitmap: Bitmap? = null) {
        _capturedSampleTag.value = sampleTag
        _capturedPhotoBitmap.value = bitmap
    }

    fun runAiAnalysis(latitude: Double = 13.0827, longitude: Double = 80.2707, userDescription: String = "") {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _currentScreen.value = Screen.CITIZEN_AI_ANALYSIS

            val result = GeminiWasteAnalyzer.analyzeWasteImage(
                bitmap = _capturedPhotoBitmap.value,
                sampleTag = _capturedSampleTag.value,
                latitude = latitude,
                longitude = longitude,
                userDescription = userDescription
            )

            _aiAnalysisResult.value = result
            _isAnalyzing.value = false
        }
    }

    fun submitNewIncident(description: String, latitude: Double = 13.0827, longitude: Double = 80.2707) {
        viewModelScope.launch {
            val res = _aiAnalysisResult.value ?: return@launch
            val newId = "WG-${(1000..9999).random()}"
            val user = _currentUser.value

            val incident = Incident(
                id = newId,
                citizenId = user.id,
                citizenName = user.name,
                photoUri = _capturedSampleTag.value ?: "sample_plastic_drain",
                wasteType = res.wasteType,
                severity = res.severity,
                riskScore = res.riskScore,
                riskLevel = res.riskLevel,
                latitude = latitude,
                longitude = longitude,
                address = "${user.ward}, M.G. Road, ${user.district}",
                district = user.district,
                municipality = user.municipality,
                ward = user.ward,
                description = description.ifBlank { "Citizen reported waste incident via mobile app." },
                status = "AI Analyzed",
                drainRisk = res.drainRisk,
                fireRisk = res.fireRisk,
                illegalDumpingProb = res.dumpingProbability,
                estimatedQuantity = res.estimatedQuantity,
                weatherNote = res.weatherNote,
                duplicateCount = (1..5).random()
            )

            repository.insertIncident(incident)
            _selectedIncidentId.value = newId
            _currentScreen.value = Screen.CITIZEN_MY_REPORTS
        }
    }

    fun selectIncident(id: String) {
        _selectedIncidentId.value = id
        _currentScreen.value = if (_currentUser.value.role == UserRole.MUNICIPAL_AUTHORITY) {
            Screen.AUTHORITY_INCIDENT_DETAIL
        } else {
            Screen.CITIZEN_MY_REPORTS
        }
    }

    fun assignTeam(incidentId: String, teamId: String, teamName: String) {
        viewModelScope.launch {
            repository.assignTeamToIncident(incidentId, teamId, teamName)
        }
    }

    fun verifyCleanup(incidentId: String) {
        viewModelScope.launch {
            repository.verifyAndResolveCleanup(incidentId, "sample_clean_street", 12)
        }
    }

    fun logout() {
        _currentScreen.value = Screen.SPLASH
    }
}
