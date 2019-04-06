package app.dao;

import app.model.Assignment;
import app.model.Users;
import app.response.Login;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import app.model.Password;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Repository
@Transactional
public class LoginDaoI implements LoginDao {

    @Autowired
    static private SessionFactory sessionFactory;

    @Value("${jwt.auth.header}")
    String authHeader;

    public LoginDaoI() {
    }

    public LoginDaoI(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean get(String email, String pass) {
        Password p = null;
        String hql = "from Password where hash='";
        hql += pass + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Password> list = (List<Password>) query.list();

            if (list != null && !list.isEmpty()) {
                p = list.get(0);
            }
        } catch (Exception e) {
            return false;
        }

        if (p != null) {

            hql = "from Users where email='" + email + "'";
            try {
                Query query = sessionFactory.getCurrentSession().createQuery(hql);

                @SuppressWarnings("unchecked")
                List<Users> list = (List<Users>) query.list();

                if (list != null && !list.isEmpty()) {

                    hql = "from Assignment where password=";
                    hql += p.getId() + " and user='" + list.get(0).getId() + "'";
                    try {
                        query = sessionFactory.getCurrentSession().createQuery(hql);

                        @SuppressWarnings("unchecked")
                        List<Assignment> list2 = (List<Assignment>) query.list();

                        if (list2 != null && !list2.isEmpty()) {
                            return true;
                        }
                    } catch (Exception e) {
                        return false;
                    }

                }
            } catch (Exception e) {
                return false;
            }
        }

        return false;

    }

    @Override
    public String logout(HttpServletRequest request) {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);

        return authHeaderVal;
    }


}
