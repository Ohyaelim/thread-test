import json
import boto3
import datetime
import time
import calendar



def lambda_handler(event, context):
    today = datetime.datetime.now()
    year= str(today.year)
    last_month= str(today.month-1)
    month = str(today.month)
    last_day = datetime.date(today.year, today.month-1, calendar.monthrange(today.year, today.month-1)[-1]).day
    
    if today.month-1 < 10 :
        last_month = '0' + last_month
    else :
        pass    
    print(last_month)
    print(year)
    
    s3_resource = boto3.resource('s3')
    s3_client = boto3.client('s3')
    glue_client = boto3.client('glue')
    my_bucket = s3_resource.Bucket('sscard')
    attribute_obj_prefix = 'attribute'
    sale_obj_prefix = 'sale'
    finish_obj_prefix = 'finish'
    attribute_file = "S{}{}_SSCARD_CUST_ATTRIBUTE.zip".format(year, last_month)
    sale_file = "S{}{}_SSCARD_CUST_CARD_SALE.zip".format(year, last_month)
    flag_filename = '{}{}_finish.txt'.format(year,month)
    tmp_key = "/tmp/"
    finish_key = "data/finish/"



   #list out all the keys in the bucket
    attribute_key_list = [key['Key'] for key in s3_client.list_objects_v2(Bucket='sscard',Prefix='data/sscard_cust_attribute/zip/')['Contents']]
    sale_key_list = [key['Key'] for key in s3_client.list_objects_v2(Bucket='sscard',Prefix='data/sscard_cust_card_sale/zip/')['Contents']]
    finish_key_list = [key['Key'] for key in s3_client.list_objects_v2(Bucket='sscard',Prefix='data/finish/')['Contents']]
    
    #extract the latest report date among the files w/ 'spend' prefix



   attribute_obj_list = [x.split('/')[-1] for x in attribute_key_list if attribute_obj_prefix in x]
for k in attribute_obj_list :
        if k == attribute_file :

           sale_obj_list = [x.split('/')[-1] for x in sale_key_list if sale_obj_prefix in x]
            for p in sale_obj_list :
                if p == sale_file :

                   finish_obj_list = [x.split('/')[-1] for x in finish_key_list if finish_obj_prefix in x]
                    for m in finish_obj_list :
                        if m != flag_filename :
                            flag_file = open(tmp_key + flag_filename, "w")
                            flag_file.close()
                            response = s3_client.upload_file(tmp_key + flag_filename, my_bucket.name, finish_key+flag_filename)
                        else :
                            pass

               else :
                    pass
        else :
            pass