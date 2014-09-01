//
//  WebViewController.m
//  FF-IOS-ScriptAuthTest
//
//  Created by David Wells on 2/17/13.
//  Copyright (c) 2013 FatFractal. All rights reserved.
//

#import "WebViewController.h"

@interface WebViewController ()

@end

@implementation WebViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    self.title = @"Login";
}

- (void)viewDidAppear:(BOOL)animated
{
    if (self.authUri) {
        NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:self.authUri]];
        self.webView.delegate = self;
        [self.webView loadRequest:request];
    } else {
        [self dismissViewControllerAnimated:YES completion:NULL];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if ([request.URL.absoluteString hasPrefix:self.callbackUri]) {
        [self.delegate webViewController:self didReceiveCallbackUriWithCode:request.URL.absoluteString];
        [self dismissViewControllerAnimated:YES completion:NULL];
        return NO;
    } else {
        return YES;
    }
}

- (IBAction)cancel:(id)sender {
    [self.delegate webViewControllerDidCancel:self];
    [self dismissViewControllerAnimated:YES completion:NULL];
}

@end
