package br.com.looplex;

import br.com.looplex.dao.user.UserDao;
import br.com.looplex.dao.user.UserDaoImpl;
import br.com.looplex.models.ElasticsearchUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ElasticsearchSecurityDAOService  {

    // TODO: DI ?
    UserDao userDao = new UserDaoImpl();

    public void create(ElasticsearchUser newInstance) {
        userDao.create(newInstance);
    }

    public List<ElasticsearchUser> findAll() {
        return userDao.findAll();
    }

    public ElasticsearchUser findById(String id) {
        return userDao.findById(id);
    }

    public ElasticsearchUser update(ElasticsearchUser transientObject) {
        return userDao.update(transientObject);
    }

    public void delete(ElasticsearchUser persistentObject) {
        userDao.delete(persistentObject);
    }


}
