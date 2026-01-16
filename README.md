# ToDo List - Task Board API

## Description

A simple API for a Todo list with a Kanban board layout and capability.
Made with a React Front-end, and a Java Back-end, with local CSV for Database management.

The UI will have the following elements:
• Input element to add a Task, with the ability to select a column (default 'To Do' col)
• Then the board with 3 Columns, the 'To Do', the 'In Progress', and the 'Done'
• each column will have it's tasks with the title and button to delete
  ◘ the tasks will be able to move between columns by dragging


## Domain:
| Task |
|:--------------|
| UUID id [PK] |
| String content |
| Long column |

_has:_
+ _\<\<enumeration\>\>_ Column
  - 'TO_DO'
  - 'IN_PROGRESS'
  - 'DONE'

## Tools
• Maven setup: https://start.spring.io \
• Domain creation: https://www.drawio.com | https://app.diagrams.net
  also for ERD: https://dbdiagram.io

## RestAPI Overview:
```
GET	    /tasks              List tasks
POST    /tasks              Create task
DELETE  /tasks/{task_id}    Delete task
```

## Built With
- Java
- React

## Project links
- [API Github repo](https://github.com/NewIncome/kanban-board-api) \
- [UI Github repo](https://github.com/NewIncome/react-kanban-board)

Author | Social Media
:--------------:|:------------:
👤 | - Github: [@NewIncome](https://github.com/NewIncome)
**Alfredo C.** | - Twitter: [@J_A_fredo](https://twitter.com/J_A_fredo)
. | - Linkedin: [Alfredo C.](https://www.linkedin.com/in/j-alfredo-c)
