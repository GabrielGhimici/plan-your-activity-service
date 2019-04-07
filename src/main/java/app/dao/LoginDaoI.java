package app.dao;

import app.dto.RegisterDTO;
import app.model.Assignment;
import app.model.Teams;
import app.model.Users;
import app.response.Details;
import app.response.Login;
import app.services.JwtService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import app.model.Password;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class LoginDaoI implements LoginDao {

    @Autowired
    static private SessionFactory sessionFactory;

    @Value("${jwt.auth.header}")
    String authHeader;

    @Autowired
    private JwtService jwtTokenService;

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

    @Override
    public boolean setPassword (HttpServletRequest request, String pass)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);
        Users u = null;


        String hql = "from Users where email='";
        hql += jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {
                u = list.get(0);
                hql = "update Users set validated='true' where id='" + u.getId() + "'";
                query = sessionFactory.getCurrentSession().createQuery(hql);
                query.executeUpdate();
            }
        }
        catch(Exception e) {
            return false;
        }

        hql = "from Password where hash='";
        hql += pass + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Password> list = (List<Password>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Assignment where user = '" + u.getId() + "'";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Assignment> list2 = (List<Assignment>) query.list();

                    if (list2 != null && !list2.isEmpty()) {

                        list2.get(0).setPassword(list.get(0).getId());
                        sessionFactory.getCurrentSession().saveOrUpdate(list2.get(0));

                        return true;

                    }

                }catch (Exception exe)
                {
                    return false;
                }

            }

        }catch (Exception e)
        {
            return false;
        }

        Password p = new Password();
        p.setPassword(pass);
        try {
            sessionFactory.getCurrentSession().save(p);
        } catch (Exception e) {
            return false;
        }

        hql = "from Assignment where user='";
        hql += u.getId() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Assignment> list2 = (List<Assignment>) query.list();

            if (list2 != null && !list2.isEmpty()) {
                sessionFactory.getCurrentSession().delete(list2.get(0));
            }

        }catch (Exception exe)
        {
            return false;
        }

        Assignment a = new Assignment();
        a.setPassword(p.getId());
        a.setUser(u.getId());

        try {
            sessionFactory.getCurrentSession().save(a);
        } catch (Exception e) {
            return false;
        }

        return true;

    }


    public boolean set(RegisterDTO user, HttpServletRequest request)
    {
        try {
            String hql = "select id from Password where hash='" + user.getPassword() + "'";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            @SuppressWarnings("unchecked")
            List<Long> list = (List<Long>) query.list();

            Password p = new Password();
            p.setPassword(user.getPassword());

            if (list == null || list.isEmpty()) {
                hql = "select MAX(id) from Password";
                query = sessionFactory.getCurrentSession().createQuery(hql);

                list = (List<Long>) query.list();

                p.setId(list.get(0) + 1);

                sessionFactory.getCurrentSession().save(p);

            }
            else
            {
                p.setId(list.get(0));
            }

            hql = "select MAX(id) from Users";
            query = sessionFactory.getCurrentSession().createQuery(hql);
            @SuppressWarnings("unchecked")
            List<Long> list2 = (List<Long>) query.list();

            Users u = new Users();
            u.setId(list2.get(0)+1);
            u.setValidate(false);
            u.setBorn(user.getBorn());
            u.setEmail(user.getEmail());
            u.setName(user.getName());

            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final String authHeaderVal = httpRequest.getHeader(authHeader);
            Login jwtUser = jwtTokenService.getUser(authHeaderVal);

            hql = "select team from Users where email='";
            hql += jwtUser.getUserName() + "'";
            query = sessionFactory.getCurrentSession().createQuery(hql);
            @SuppressWarnings("unchecked")
            List<Long> list3 = (List<Long>) query.list();

            u.setTeam(list3.get(0));

            sessionFactory.getCurrentSession().save(u);

            Assignment a = new Assignment();
            a.setUser(u.getId());
            a.setPassword(p.getId());

            sessionFactory.getCurrentSession().save(a);

            return true;
        }
        catch (Exception e)
        {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Details getDetails(HttpServletRequest request) {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);
        Users u = null;
        String hql = "from Users where email='";
        hql += jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {
                u = list.get(0);
            }
        }
        catch(Exception e) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Details d = new Details();
        d.setBorn(formatter.format(u.getBorn()));
        d.setEmail(u.getEmail());
        d.setPermanentAccount(u.isValidate());
        d.setName(u.getName());


        Teams t = null;
        hql = "from Teams where id='";
        hql += u.getTeam() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Teams> list = (List<Teams>) query.list();

            if (list != null && !list.isEmpty()) {
                t = list.get(0);
            }
        }
        catch(Exception e) {
            return null;
        }

        d.getTeam().setId(t.getId());
        d.getTeam().setName(t.getName());
        if(u.getId() == t.getLeader())
            d.getTeam().setLeader(true);

        return d;

    }

    @Override
    public Details setDetails (Details user, HttpServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        Users u = null;
        String hql = "from Users where email='";
        hql += jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {
                u = list.get(0);
                hql = "update Users set name='" + user.getName() + "', email = '" + user.getEmail() + "', born = '" + user.getBorn()
                        + "' where id='" + u.getId() + "'";
                query = sessionFactory.getCurrentSession().createQuery(hql);
                query.executeUpdate();

                return user;
            }
        }
        catch(Exception e) {
            return null;
        }

        return null;
    }
}
