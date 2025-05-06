package AndrewWebServices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AndrewWebServicesTest {
    private InMemoryDatabase fakeDatabase;

    @Mock
    private RecSys recommender;

    @Mock
    private PromoService promoService;

    private AndrewWebServices andrewWebService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        fakeDatabase = new InMemoryDatabase();

        andrewWebService = new AndrewWebServices(fakeDatabase, recommender, promoService);
    }

    @Test
    public void testLogInSuccess() {
        assertTrue(andrewWebService.logIn("Scotty", 17214));
    }

    @Test
    public void testLogInFailure() {
        assertFalse(andrewWebService.logIn("Scotty", 12345));

        assertFalse(andrewWebService.logIn("NonExistentUser", 12345));
    }

    @Test
    public void testGetRecommendation() {
        when(recommender.getRecommendation("Scotty")).thenReturn("Star Wars");

        assertEquals("Star Wars", andrewWebService.getRecommendation("Scotty"));

        verify(recommender, times(1)).getRecommendation("Scotty");
    }

    @Test
    public void testSendEmail() {
        String testEmail = "test@example.com";
        andrewWebService.sendPromoEmail(testEmail);

        verify(promoService, times(1)).mailTo(testEmail);
    }

    @Test
    public void testNoSendEmail() {
        verifyNoInteractions(promoService);

        andrewWebService.logIn("Scotty", 17214);

        verifyNoInteractions(promoService);
    }
}
