package com.example.paisehpay;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.paisehpay.databaseHandler.FirebaseAdapter;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    DatabaseReference mockRef;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testAdapter() {
        FirebaseAdapter adapter = new FirebaseAdapter();
        // Use Mockito to mock Firebase behavior
    }

}