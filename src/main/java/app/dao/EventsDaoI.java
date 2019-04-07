package app.dao;

import app.dto.EventDTO;
import app.model.Attendants;
import app.model.Events;
import app.model.Teams;
import app.model.Users;
import app.response.EventsPOJO;
import app.response.Login;
import app.response.UserPOJO;
import app.services.JwtService;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public class EventsDaoI implements EventsDao {

    @Autowired
    static private SessionFactory sessionFactory;

    @Value("${jwt.auth.header}")
    String authHeader;

    @Autowired
    private JwtService jwtTokenService;

    public EventsDaoI() {
    }

    public EventsDaoI(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public EventsPOJO addEvent(EventDTO event, HttpServletRequest request)
    {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from Users where email = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {

                try {

                    EventsPOJO ev = new EventsPOJO();

                    Events e = new Events();
                    e.setDescription(event.getDescription());
                    e.setCreator(list.get(0).getId());
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                    e.setCdate(ft.parse(event.getStart_date() + " " + event.getStart_time()));
                    e.setFdate(ft.parse(event.getFinish_date() + " " + event.getFinish_time()));

                    Date d = new Date();
                    if(e.getCdate().after(d) && e.getFdate().after(e.getCdate())) {

                        sessionFactory.getCurrentSession().save(e);


                        ev.setStart_time(event.getStart_time());
                        ev.setFinish_time(event.getFinish_time());
                        ev.setId(e.getId());
                        ev.setDescription(e.getDescription());
                        ev.setStart_date(event.getStart_date());
                        ev.setFinish_date(event.getFinish_date());

                        for(int i=0; i < event.getAttendants().length; i++)
                        {

                            Attendants a = new Attendants();

                            a.setEvent(e.getId());
                            a.setInvited_user(Integer.valueOf(event.getAttendants()[i].getId()));

                            sessionFactory.getCurrentSession().save(a);
                        }

                    }
                    else
                    {
                        return null;
                    }

                    return ev;

                }
                catch (Exception e)
                {
                    return null;
                }

            }
        }catch (Exception e)
        {
            return null;
        }

        return null;

    }

    @Override
    public UserPOJO[] getUsers(HttpServletRequest request)
    {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from Users where email <> '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {

                UserPOJO[] vect = new UserPOJO[list.size()];

                for(int i=0 ;i < list.size(); i++) {

                    hql = "from Teams where id='" + list.get(i).getTeam() + "'";

                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Teams> list2 = (List<Teams>) query.list();

                    UserPOJO u = new UserPOJO();
                    u.setId(list.get(i).getId());
                    u.setName(list.get(i).getName());
                    u.setTeam(list2.get(0).getName());
                    vect[i]= u;
                }

                return vect;
            }

            return null;
        }
        catch(Exception e) {
            return null;
        }
    }

}

