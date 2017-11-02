## To build the image:
1- Make sure you have the latest fat jar in the path. You can copy that from the target folder. 
2- docker build -f Dockerfile -t images:hp .

## To run a container:
Mount a volume to be able to pass the file as an argument 
docker run -v=/home/amjad/hp/:/data --rm  images:hp  /data/Steal_Master_Key_unfolded_trimmed.adt

