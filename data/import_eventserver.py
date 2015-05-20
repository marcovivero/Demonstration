import predictionio
import argparse



def import_events(client, filename):
    count  = 0
    print("Importing data.............")
    f = open("./data/" + filename, "r").read().split("\n")[: -1]
    for elem in f:
        count += 1
        client.create_event(
            event = "data",
            entity_id = count,
            entity_type = "customer",
            properties = {
                "items" : elem
            }
        )
    print("Imported {0} events.".format(count))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        description="Import sample data for text manipulation engine")
    parser.add_argument('--file', default = 'testdata.txt')
    parser.add_argument('--access_key', default='invald_access_key')
    parser.add_argument('--url', default="http://localhost:7070")
    args = parser.parse_args()

    client = predictionio.EventClient(
        access_key=args.access_key,
        url=args.url,
        threads=20,
        qsize=5000)

    import_events(client, args.file)
