package com.chelseatroy.canary.data

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoodEntryScatterAnalysisTest {
    lateinit var systemUnderTest: MoodEntryScatterAnalysis

    @Before
    fun setUp() {
        systemUnderTest = MoodEntryScatterAnalysis()
    }

    @Test
    fun arragesMoodsVertically_basedOnMoodHappiness() {
        assertEquals(1f, systemUnderTest.getYPositionFor(MoodEntry(Mood.UPSET)))
        assertEquals(2f, systemUnderTest.getYPositionFor(MoodEntry(Mood.DOWN)))
        assertEquals(3f, systemUnderTest.getYPositionFor(MoodEntry(Mood.NEUTRAL)))
        assertEquals(4f, systemUnderTest.getYPositionFor(MoodEntry(Mood.COPING)))
        assertEquals(5f, systemUnderTest.getYPositionFor(MoodEntry(Mood.ELATED)))
    }

    @Test
    fun arrangesMoodsHorizontally_proportionalToWhenTheyWereLogged() {
        val regularlySpacedMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 2L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 3L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 4L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 5L, "Note", "EATING")
        )

        assertEquals(arrayListOf(0.0f, 0.25f, 0.5f, 0.75f, 1.0f), systemUnderTest.getXPositionsFor(regularlySpacedMoodEntries))
    }

    @Test
    fun comments_whenThereArentManyMoodEntries_encourageMoreMoodLogging() {
        val fewMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.COPING,1L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 4L, "Note", "EATING")
        )

        val commentText = systemUnderTest.commentOn(fewMoodEntries)
        assertEquals(commentText, MoodEntryScatterAnalysis.COMMENT_MINIMUM_ENTRIES)
    }

    @Test
    fun comments_whenMoodIsImproving_mentionThatMoodIsImproving() {

        val increasingMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED,1L, "Note", "EATING"),
            MoodEntry(Mood.COPING,2L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,3L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,4L, "Note", "EATING"),
            MoodEntry(Mood.DOWN,5L, "Note", "EATING"),
            MoodEntry(Mood.UPSET, 6L, "Note", "EATING")
        )


        val commentText = systemUnderTest.commentOn(increasingMoodEntries)

        assertEquals(commentText,MoodEntryScatterAnalysis.COMMENT_INCREASING_MOOD)

//        fail("If the person's mood, in general, is better lately than it was earlier in the week " +
//                "produce a message mentioning it. " +
//                "You get to decide how you want to measure this.")
//        // MEASURE by taking last 3 (d, d-1, d-2) days avg and compare to 3 day average from
        // d-4,d-5,d-6
    }

    @Test
    fun comments_whenMoodIsDeclining_mentionThatMoodIsDeclining() {

        val decreasingMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED,6L, "Note", "EATING"),
            MoodEntry(Mood.COPING,5L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,4L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,3L, "Note", "EATING"),
            MoodEntry(Mood.DOWN,2L, "Note", "EATING"),
            MoodEntry(Mood.UPSET, 1L, "Note", "EATING")
        )

        val commentText = systemUnderTest.commentOn(decreasingMoodEntries)

        assertEquals(commentText, MoodEntryScatterAnalysis.COMMENT_DECREASING_MOOD)
//        fail("If the person's mood, in general, is not as good lately as it was earlier in the week " +
//                "produce a message mentioning it. " +
//                "You get to decide how you want to measure this.")
    }

    @Test
    fun comments_whenThereIsOneOutlierMood_mentionAnyNotesFromThatMood() {

        val outlierMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.NEUTRAL,6L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,5L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,4L, "Note", "EATING"),
            MoodEntry(Mood.NEUTRAL,3L, "Note", "EATING"),
            MoodEntry(Mood.ELATED,2L, "I walked the dog today and won the lottery!", "EATING"),
            MoodEntry(Mood.NEUTRAL, 1L, "Note", "EATING")
        )

        val commentText = systemUnderTest.commentOn(outlierMoodEntries)
        assertEquals(commentText, MoodEntryScatterAnalysis.COMMENT_OUTLIER_MOOD)
//        fail("If the person's mood, in general, is stable, and there is ONE outlier, " +
//                "produce a message containing the notes for that mood." +
//                "You get to decide how you want to go about this.")
    }


}