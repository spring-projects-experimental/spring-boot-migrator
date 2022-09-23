**Features used in the current project**

* ehcache in pom is mentioned this helps validate our migration to spring 3 where ehcache classifier has to be used to resolve version.
* Uses Constructor binding on classes which will be removed when migrating to Spring 3
* Uses PagingAndSortingRepo interface where CrudRepo has been removed.
* Uses properties that will be removed/moved on spring 3 migration
* Uses micrometer packages which are moved to a new structure.

