package com.zn.google_android_dev_exam_practice.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;

/**
 * Repositories abstract access to multiple data sources, if your app has any. It is a convenience
 * class that handles data operations and liaising updates between the Dao and a Network. Access
 * your database through the repository object, rather than directly, so if you have multiple data
 * sources, this single repository can handle interacting with all of them for you. That being said,
 * this seems like a "Content Provider", doesn't it?
 * <p>
 * It is common to assess whether to update data and fetch fresh data, or to provide cached data
 */
public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<PagedList<Task>> mAllTasks;

    private final static int PAGE_SIZE = 10;

    TaskRepository(Application application, SortingTasks sort) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        // To get LiveData PagedLists from dao, use the LivePagedListBuilder<>. This is where you
        // specify the sizes of each page
        switch (sort) {
            case DATE_ASC:
                mAllTasks = new LivePagedListBuilder<>(mTaskDao.getAllTasks(),
                        PAGE_SIZE).build();
                break;
            case DATE_DESC:
                mAllTasks = new LivePagedListBuilder<>(mTaskDao.getAllTasksDescendingDueDate(),
                        PAGE_SIZE).build();
                break;
            case NAME_ASC:
                mAllTasks = new LivePagedListBuilder<>(mTaskDao.getAllTasksAscendingName(),
                        PAGE_SIZE).build();
                break;
            case NAME_DESC:
                mAllTasks = new LivePagedListBuilder<>(mTaskDao.getAllTasksDescendingName(),
                        PAGE_SIZE).build();
                break;
            case STATUS_ASC:
                mAllTasks = new LivePagedListBuilder<>(mTaskDao.getAllTasksAscendingStatus(),
                        PAGE_SIZE).build();
                break;
            case STATUS_DESC:
                mAllTasks = new LivePagedListBuilder<>(mTaskDao.getAllTasksDescendingStatus(),
                        PAGE_SIZE).build();
                break;
        }
    }

    LiveData<PagedList<Task>> getAllTasks() {
        return mAllTasks;
    }

    enum SortingTasks {
        DATE_ASC, DATE_DESC, NAME_ASC, NAME_DESC, STATUS_ASC, STATUS_DESC
    }

    public void insert(Task task) {
        new InsertAsyncTask(mTaskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateAsyncTask(mTaskDao).execute(task);
    }

    public void delete(long id) {
        new DeleteAsyncTask(mTaskDao).execute(id);
    }

    /**
     * Use an AsyncTask to properly insert a new task into the database
     */
    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        InsertAsyncTask(TaskDao taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.insert(tasks[0]);
            return null;
        }
    }

    /**
     * Use an AsyncTask to properly update an existing task in the database
     */
    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        UpdateAsyncTask(TaskDao taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.update(tasks[0]);
            return null;
        }
    }

    /**
     * Use an AsyncTask to properly update an existing task in the database
     */
    private static class DeleteAsyncTask extends AsyncTask<Long, Void, Void> {
        private TaskDao mAsyncTaskDao;

        DeleteAsyncTask(TaskDao taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Long... ids) {
            mAsyncTaskDao.delete(ids[0]);
            return null;
        }
    }

}
