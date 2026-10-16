# Git Workflow Guide for Team Members

## 1. Clone the Repository

Run this once to download the repository to your computer:

```bash
git clone <Repository-URL>
```

Then move into the project directory:

```bash
cd <repository-folder>
```

## 2. Check Your Current Git Status

Before making changes or pulling updates, always run:

```bash
git status
```

This shows which files have been modified, staged, or are untracked.

**Important:** Always check `git status` before running `git pull origin <branch-name>`.

## 3. Check Available Branches

To list local and remote branches:

```bash
git branch -a
```

## 4. Create or Switch to Your Branch

Create a new branch and switch to it:

```bash
git checkout -b <branch-name>
```

To switch to an existing branch:

```bash
git checkout <branch-name>
```

For example:

```bash
git checkout development
```

## 5. Get the Latest Changes Before Coding

Every time you start working, check your status first:

```bash
git status
```

Then pull the latest changes for the branch you are working on:

```bash
git pull origin <branch-name>
```

Replace `<branch-name>` with your actual branch name, such as `development` or `feature/approval`.

**Important:** Make sure you are on the correct branch before pulling. If you have uncommitted changes, commit them or safely stash them before pulling to avoid conflicts or accidental loss of work.

## 6. Save and Push Your Code

After coding, check your changes:

```bash
git status
```

Stage the changes:

```bash
git add .
```

Commit them with a meaningful message:

```bash
git commit -m "Describe your changes"
```

Push your branch to GitHub:

```bash
git push origin <branch-name>
```

Replace `<branch-name>` with your actual branch name.

## 7. Team Workflow Notice

> **Do not click the “Compare & pull request” button.**
>
> Your responsibility is to push your branch and code to GitHub. The designated person will handle the next stage of the workflow, including reviewing and merging changes.

## 8. Quick Command Checklist

Use this sequence when starting and finishing a coding session.

### Before coding

```bash
git status
git branch
git pull origin <branch-name>
```

### After coding

```bash
git status
git add .
git commit -m "Describe your changes"
git push origin <branch-name>
```

## Key Reminders

- Always check `git status` before pulling.
- Confirm that you are on the correct branch before pulling or pushing.
- Pull the latest changes from the branch you are working on before you start coding.
- Commit your work with a clear message.
- Push only to your assigned branch.
- Do not merge feature branches into `master` yourself.
- Do not click **Compare & pull request**; the designated person handles the next stage.
