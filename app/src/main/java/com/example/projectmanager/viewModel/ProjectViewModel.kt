package com.example.projectmanager.viewModel

import androidx.lifecycle.*
import com.example.projectmanager.model.Project
import com.example.projectmanager.model.Task
import com.example.projectmanager.repository.Repository
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProjectViewModel : ViewModel(), DefaultLifecycleObserver {
    private val repository by lazy { Repository() }

//    fun getCurrentUser() = repository.getCurrentUserName()
//    fun getCurrentUserEmail() = repository.getCurrentUserEmail()

    /**
     * Get a list of hash map for project's name and status.
     *
     * @param userName the manager's name
     *
     * @return a LiveData object for the ArrayList of HashMap.
     *         Each HashMap has keys "name" for project's name and "status" for project's status.
     *
     */
    fun getProjectsStatus(userName: String, direction: Direction = Direction.DESCENDING) = Transformations.map(repository.onProjectsChange(userName, direction.name)) { projects ->
        val list = ArrayList<HashMap<String, String>>()
        for (project in projects) {
            list.add(
                hashMapOf(
                    "name" to project.name,
                    "status" to (if (project.taskCount == project.completeCount) "complete" else "ongoing")
                )
            )
        }
        list
    }

    enum class Direction {
        ASCENDING,
        DESCENDING
    }

    fun getProjectsByName(projectName: String) = Transformations.map(repository.getProjectByName(projectName)) { project ->
        hashMapOf(
            "name" to project.name,
            "manager" to project.manager,
            "members" to project.members,
            "description" to project.description,
            "startTime" to project.startTime,
            "deadline" to project.deadline,
            "status" to (if (project.taskCount == project.completeCount) "complete" else "ongoing")
        )
    }

    fun getProjectMembers(userName: String, direction: Direction = Direction.DESCENDING) = Transformations.map(repository.onProjectsChange(userName, direction.name)) { projects ->
        val list = ArrayList<HashMap<String, Any>>()
        for (project in projects) {
            list.add(
                hashMapOf(
                    "name" to project.name,
                    "manager" to project.manager,
                    "description" to project.description,
                    "startTime" to project.startTime,
                    "deadline" to project.deadline,
                    "status" to (if (project.taskCount == project.completeCount) "complete" else "ongoing")
                )
            )
        }
        list
    }

    /**
     * Create a new Project.
     *
     * This method creates only one task for each call.
     *
     * @param projectName           project's name
     * @param managerName           manager's name
     * @param projectDescription    description of the project
     * @param projectDdl            project's deadline
     * @param taskName              task's name
     * @param taskMemberName        name of the member responsible for this task
     * @param taskDdl               task's deadline
     *
     */
    fun addProject(
        projectName: String,
        managerName: String,
        projectDescription: String,
        projectDdl: String,
    ) {
        repository.addProject(Project(projectName, managerName, arrayListOf(managerName), projectDescription, "", projectDdl, 0, 0,"ongoing"), {}, {})
//        repository.addTask(Task(taskName, projectName, taskMemberName, "on going", taskDdl), {}, {})
    }

    /**
     * Get all tasks in a project.
     *
     * @param userName      the name current user
     * @param projectName   name of the project
     *
     * @return a LiveData object for the ArrayList of HashMap.
     *         Each HashMap has keys "project" for the project the task belongs to, "task" for task's name,
     *         "member" for the member responsible for this task, "ddl" for task's deadline and "status" for task's status
     */
    fun getTasks(projectName: String) =
        Transformations.map(repository.onTasksChange(projectName)) { tasks ->
            val list = ArrayList<HashMap<String, String>>()
            for (task in tasks) {
                list.add(hashMapOf("project" to task.project, "task" to task.name, "member" to task.member, "ddl" to task.deadline, "status" to task.status))
            }
            list
        }

    fun addTask(taskName: String, projectName: String, taskMemberName: String, taskDdl: String) =
        repository.addTask(Task(taskName, projectName, taskMemberName, "assigned", taskDdl), {}, {})

    /**
     * Change the task's status to "complete".
     *
     * @param projectName   the name of the project that the task belongs to
     * @param taskName      the name of the task whose status is needed to be changed
     */
    fun setTaskToComplete(projectName: String, taskName: String) {
        repository.updateTask(projectName, taskName, "status", "complete", {}, {})
        repository.incrementProjectCounter(projectName, "completeCount", {}, {})
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        repository.stopListeningForUsersChanges()
        repository.stopListeningForProjectsChanges()
        repository.stopListeningForProjectChanges()
        repository.stopListeningForTasksChangesForProject()
    }
}
