# aegiseculock
This simple program is a joke file manager that is supposed to provide the service of locking your files with a password for privacy (it just removes all permissions using `chmod`).

## Usage
Compile with Leiningen and run; you'll see:
```
Enter a password for first-time setup:
```
Enter a password of your choice. This is the **Master Password^TM^**. The main menu should then show up:

```
CHOOSE:
1 -> Reset Master Password
2 -> New File
3 -> Read File
4 -> Write to File
5 -> Change File Password
6 -> Exit
```

You can create new files with `2` (which will also prompt you to enter a file-specific password), and read or write content to it with `3` or `4` respectively (where you enter that password). The **Master Password^TM^**... is just there to look legit.

The files are saved as dotfiles, and so are invisible on a normal `ls`. Try to read them, and you're faced with a permissions error!... It's just `chmod`ed out of all permissions; you can easily `chmod` them back on your own accord. In fact, the program itself `chmod`s every file it needs to interact with so that it can read/write it itself, before removing all permissions again. This is of course handled by the aptly-named `sophisticated-permissions-security-procedure-injection` macro.

## Raison d'etre
This program was just an experiment by me for fun, of course. I had just come out of Haskell confused by how functional languages interact with IO, and since I was interested in Clojure, wanted to see if writing an IO-related, file-handling program using it would be just as hard. It thankfully wasn't.