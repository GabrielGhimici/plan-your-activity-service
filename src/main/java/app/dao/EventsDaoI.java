package app.dao;

import app.dto.EventDTO;
import app.dto.EventUpdateDTO;
import app.model.Attendants;
import app.model.Events;
import app.model.Teams;
import app.model.Users;
import app.response.EventsPOJO;
import app.response.Invitations;
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

    @Override
    public EventsPOJO updateEvent(EventUpdateDTO event)
    {

        String hql = "from Events where id = '" + event.getId() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Events> list = (List<Events>) query.list();

            if (list != null && !list.isEmpty()) {

                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                list.get(0).setDescription(event.getDescription());
                list.get(0).setCdate(ft.parse(event.getStart_date() + " " + event.getStart_time()));
                list.get(0).setFdate(ft.parse(event.getFinish_date() + " " + event.getFinish_time()));

                if (list.get(0).getFdate().after(list.get(0).getCdate()))
                {
                    Date d = new Date();

                    if(list.get(0).getCdate().compareTo(list.get(0).getCdate()) == 0 || list.get(0).getCdate().after(d))
                    {

                        sessionFactory.getCurrentSession().update(list.get(0));

                        EventsPOJO ev = new EventsPOJO();
                        ev.setId(list.get(0).getId());
                        ev.setDescription(list.get(0).getDescription());
                        ev.setStart_time(event.getStart_time());
                        ev.setFinish_time(event.getFinish_time());
                        ev.setStart_date(event.getStart_date());
                        ev.setFinish_date(event.getFinish_date());

                        return ev;

                    }
                    else
                    {
                        return null;
                    }
                }
                else
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
    public boolean deleteEvent(EventUpdateDTO event, HttpServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        boolean creator = false;
        Users u = null;

        String hql = "from Users where email = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {

                u = list.get(0);

                hql = "from Events where id = '" + event.getId() + "' and creator = '" + list.get(0).getId() + "'";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Events> list2 = (List<Events>) query.list();

                    if (list2 != null && !list2.isEmpty()) {
                        creator = true;
                    }
                }
                catch (Exception e)
                {
                    return false;
                }

            }
        }catch (Exception e)
        {
            return false;
        }

        if(creator == true)
            hql = "from Attendants where event = '" + event.getId() + "'";
        else
            hql = "from Attendants where event = '" + event.getId() + "' and invited_user= '" + u.getId() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Attendants> list = (List<Attendants>) query.list();

            if (list != null && !list.isEmpty()) {

                for (int i = 0; i < list.size(); i++) {

                    try {
                        sessionFactory.getCurrentSession().delete(list.get(i));
                    } catch (Exception e) {
                        return false;
                    }
                }
            }

            if(creator == true) {

                hql = "from Events where id = '" + event.getId() + "'";
                try {
                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Events> list2 = (List<Events>) query.list();

                    if (list2 != null && !list2.isEmpty()) {

                        try {
                            sessionFactory.getCurrentSession().delete(list2.get(0));
                        } catch (Exception e) {
                            return false;
                        }

                    }
                } catch (Exception e) {
                    return false;
                }

            }

            return true;

        }catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public Invitations[] getInvitations(HttpServletRequest request){

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
        Login jwtUser = jwtTokenService.getUser(authHeaderVal);

        String hql = "from Users where email = '" + jwtUser.getUserName() + "'";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);

            @SuppressWarnings("unchecked")
            List<Users> list = (List<Users>) query.list();

            if (list != null && !list.isEmpty()) {

                hql = "from Attendants where invited_user = '" + list.get(0).getId() + "' and answer = '0'";
                try {

                    query = sessionFactory.getCurrentSession().createQuery(hql);

                    @SuppressWarnings("unchecked")
                    List<Attendants> list2 = (List<Attendants>) query.list();

                    if (list2 != null && !list2.isEmpty()) {

                        Invitations[] vect = new Invitations[list2.size()];

                        for (int i = 0; i < list2.size(); i++) {
                            hql = "from Events where id = '" + list2.get(i).getEvent() + "'";
                            try {

                                query = sessionFactory.getCurrentSession().createQuery(hql);

                                @SuppressWarnings("unchecked")
                                List<Events> list3 = (List<Events>) query.list();

                                if (list3 != null && !list3.isEmpty()) {

                                    Invitations inv = new Invitations();
                                    inv.setId(list2.get(i).getId());

                                    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                                    SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");

                                    EventsPOJO e = new EventsPOJO();

                                    e.setDescription(list3.get(0).getDescription());
                                    e.setId(list3.get(0).getId());

                                    Date aux = list3.get(0).getCdate();
                                    e.setStart_date(day.format(aux));
                                    e.setStart_time(time.format(aux));

                                    aux = list3.get(0).getFdate();
                                    e.setFinish_date(day.format(aux));
                                    e.setFinish_time(time.format(aux));

                                    inv.setEvent(e);

                                    hql = "from Users where id = '" + list3.get(0).getCreator() + "'";
                                    query = sessionFactory.getCurrentSession().createQuery(hql);

                                    @SuppressWarnings("unchecked")
                                    List<Users> listU = (List<Users>) query.list();

                                    if (listU != null && !listU.isEmpty()) {

                                        inv.setCreator(listU.get(0).getName());

                                    }
                                    else
                                    {
                                        return null;
                                    }

                                    vect[i] = inv;
                                }
                                else
                                {
                                    return null;
                                }

                            } catch (Exception e) {
                                return null;
                            }

                        }

                        return vect;
                    }

                } catch (Exception e) {
                    return null;
                }
            }
        }catch (Exception e)
        {
            return null;
        }

        return null;

    }
}

