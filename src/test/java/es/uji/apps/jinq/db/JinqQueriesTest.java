package es.uji.apps.jinq.db;

import es.uji.apps.jinq.db.Detail;
import es.uji.apps.jinq.db.Master;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.orm.stream.JinqStream;
import org.jinq.tuples.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JinqQueriesTest
{
    public static final int QUERY_LIMIT = 5;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void entityManagerExists() throws Exception
    {
        assertThat(entityManager, not(nullValue()));
    }

    @Test
    public void streamProviderCanBeCreated() throws Exception
    {
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
        assertThat(streams, not(nullValue()));
    }

    @Test
    public void canQuerySimpleTables() throws Exception
    {
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
        JinqStream<Master> reportsStream = streams.streamAll(entityManager, Master.class);

        List<Master> master = reportsStream
                .limit(QUERY_LIMIT)
                .toList();

        assertThat(master.size(), is(QUERY_LIMIT));
    }

    @Test
    public void canQueryOnlyFewColumns() throws Exception
    {
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
        JinqStream<Master> reportsStream = streams.streamAll(entityManager, Master.class);

        List<Pair<Long, String>> records = reportsStream
                .select(master -> new Pair<>(master.getId(), master.getName()))
                .limit(QUERY_LIMIT)
                .toList();

        assertThat(records.size(), is(QUERY_LIMIT));
    }

    @Test
    public void canQueryWithFilters() throws Exception
    {
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
        JinqStream<Master> reportsStream = streams.streamAll(entityManager, Master.class);

        List<Master> records = reportsStream
                .where(master -> master.getId() <= QUERY_LIMIT)
                .toList();

        assertThat(records.size(), is(QUERY_LIMIT));
    }

    @Test
    public void canQueryJoiningTables() throws Exception
    {
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
        JinqStream<Detail> reportsStream = streams.streamAll(entityManager, Detail.class);

        List<Pair<Detail, Master>> records = reportsStream
                .select(detail -> new Pair<>(detail, detail.getMaster()))
                .where(detail -> detail.getOne().getId() <= QUERY_LIMIT)
                .toList();

        records.stream().forEach(System.out::println);

        assertThat(records.size(), is(QUERY_LIMIT));
    }

    @Test
    public void canQueryJoiningTablesWithFetch() throws Exception
    {
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
        JPAJinqStream<Master> reportsStream = streams.streamAll(entityManager, Master.class);

        List<Master> records = reportsStream
                .where(master -> master.getId() <= QUERY_LIMIT)
                .joinFetchList(master -> master.getDetails())
                .toList();

        records.stream().forEach(System.out::println);

        assertThat(records.size(), is(QUERY_LIMIT));
    }
}