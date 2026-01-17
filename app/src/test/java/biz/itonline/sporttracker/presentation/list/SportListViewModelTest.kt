package biz.itonline.sporttracker.presentation.list

import biz.itonline.sporttracker.MainDispatcherRule
import biz.itonline.sporttracker.domain.model.SportRecord
import biz.itonline.sporttracker.domain.model.StorageType
import biz.itonline.sporttracker.domain.repository.SportRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class SportListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<SportRepository>()

    @Test
    fun `Load data - default filter is ALL - shows all items`() = runTest {
        // 1. GIVEN
        val localRecord = SportRecord(id = "1", name = "Běh", place = "Les", durationMinutes = 30, type = StorageType.LOCAL, date = Date())
        val remoteRecord = SportRecord(id = "2", name = "Plavání", place = "Bazén", durationMinutes = 60, type = StorageType.REMOTE, date = Date())

        every { repository.getAllSports() } returns flowOf(listOf(localRecord, remoteRecord))

        val viewModel = SportListViewModel(repository)

        backgroundScope.launch {
            viewModel.uiState.collect()
        }

        // --- ZMĚNA: Počkáme, až se všechno "usadí" ---
        advanceUntilIdle()

        // 2. THEN
        val state = viewModel.uiState.value
        assertTrue("State should be Success, but was $state", state is SportListUiState.Success)
        assertEquals(2, (state as SportListUiState.Success).sports.size)
    }

    @Test
    fun `Filter change - switch to LOCAL - shows only local items`() = runTest {
        // 1. GIVEN
        val localRecord = SportRecord(id = "1", name = "Běh", place = "Les", durationMinutes = 30, type = StorageType.LOCAL, date = Date())
        val remoteRecord = SportRecord(id = "2", name = "Plavání", place = "Bazén", durationMinutes = 60, type = StorageType.REMOTE, date = Date())
        every { repository.getAllSports() } returns flowOf(listOf(localRecord, remoteRecord))

        val viewModel = SportListViewModel(repository)

        backgroundScope.launch {
            viewModel.uiState.collect()
        }

        // Počkáme na načtení prvních dat
        advanceUntilIdle()

        // 2. WHEN
        viewModel.onFilterChanged(FilterType.LOCAL)

        // --- ZMĚNA: Počkáme znovu po změně filtru, aby se stihl přepočítat combine ---
        advanceUntilIdle()

        // 3. THEN
        val state = viewModel.uiState.value
        assertTrue("State should be Success, but was $state", state is SportListUiState.Success)

        val sports = (state as SportListUiState.Success).sports
        assertEquals(1, sports.size)
        assertEquals(StorageType.LOCAL, sports.first().type)
    }

    @Test
    fun `Load data - empty list - shows success state with empty list`() = runTest {
        // 1. GIVEN - Repozitář vrátí prázdný seznam (ne chybu, ale 0 dat)
        every { repository.getAllSports() } returns flowOf(emptyList())

        val viewModel = SportListViewModel(repository)

        backgroundScope.launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        // 2. THEN - Měl by to být Success, ale prázdný
        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is SportListUiState.Success)

        val sports = (state as SportListUiState.Success).sports
        assertTrue("List should be empty", sports.isEmpty())
    }
}