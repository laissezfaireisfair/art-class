#!/bin/bash
set -euxo pipefail

walk_dir () {
    for pathname in "$1"/*; do
        if [ -d "$pathname" ]; then
            walk_dir "$pathname"
        else
            case "$pathname" in
                *.jpg|*.png)
                    mogrify -resize 640x640 "$pathname" || echo "$pathname" > failed.txt
            esac
        fi
    done
}

walk_dir "dataset"
