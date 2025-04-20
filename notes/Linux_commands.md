Commands to remember.

Eliminate pwd: `export PS1='> '`

### Docker ###
`docker exec -it mycontainer sh`: open shell
`docker build -t image_name:image_tag .`
`docker image rm`
`docker compose build`
`docker compose run --rm` = remove container after it stops;
`docker compose up`


### Navigating ###
`cd -` : return to the previous folder you were in
`cd //`: go to root folder

### Files & folders###
**Remove all folders with the given name from all subdirectories**:
`find . -name file_name -exec rm -r {} \;`
Add `-mindepth 2` to ignore the folder in the current directory.

**Use tree to visually display folder structure**

### Git ###
**Removing a folder from repo history:** (used on single branch, single user).
```
git filter-repo --path target_folder --invert-paths
git remote add origin path_to_repo.git
git push --set-upstream origin main
git push --force
```

### Misc ###
**Crop all images in the current folder (with ImageMagick)**
`for img in *.PNG; do convert "$img" -crop +20+40 -crop -20-20 +repage "cropped_$img"; done`
left-top right-down 




