//package SignupFunctionality;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import dao.AdminDAO;
//import model.AdminEntity;
//import util.PasswordUtil;
//
//import java.util.Optional;
//
//public class SignupTest {
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    @Mock
//    private HttpSession session;
//    @Mock
//    private AdminDAO adminDAO;
//
//    private static final String VALID_EMAIL = "test@example.com";
//    private static final String VALID_PASSWORD = "password123";
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(request.getSession()).thenReturn(session);
//    }
//
//    @Test
//    void testSuccessfulSignup() throws Exception {
//        when(request.getParameter("email")).thenReturn(VALID_EMAIL);
//        when(request.getParameter("password")).thenReturn(VALID_PASSWORD);
//        when(adminDAO.getByEmail(VALID_EMAIL)).thenReturn(null);
//
//        String hashedPassword = PasswordUtil.hashPassword(VALID_PASSWORD);
//        AdminEntity admin = new AdminEntity();
//        admin.setEmail(VALID_EMAIL);
//        admin.setPassword(hashedPassword);
//
//        adminDAO.register(admin);
//
//        verify(response).sendRedirect("login.jsp");
//        verify(request.getSession()).setAttribute("message", "Signup successful! Please login");
//    }
//
//    @Test
//    void testDuplicateEmailSignup() throws Exception {
//        when(request.getParameter("email")).thenReturn(VALID_EMAIL);
//        when(request.getParameter("password")).thenReturn(VALID_PASSWORD);
//        when(adminDAO.getByEmail(VALID_EMAIL)).thenReturn(Optional.of(new AdminEntity()));
//
//        verify(response).sendRedirect("signup.jsp");
//        verify(request.getSession()).setAttribute("error", "Email already exists.");
//    }
//
//    @Test
//    void testEmptyFieldsSignup() throws Exception {
//        when(request.getParameter("email")).thenReturn("");
//        when(request.getParameter("password")).thenReturn("");
//
//        verify(response).sendRedirect("signup.jsp");
//        verify(request.getSession()).setAttribute("error", "Email and password cannot be empty.");
//    }
//}
